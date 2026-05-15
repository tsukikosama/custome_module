/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.continew.admin.controller.biz.model.entity.ActivityMemberDO;
import top.continew.admin.customer.event.SendMessageEvent;
import top.continew.admin.hrcommon.mapper.ActivityMapper;
import top.continew.admin.hrcommon.mapper.ActivityMemberMapper;
import top.continew.admin.hrcommon.mapper.NoticeMapper;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.ActivityDO;
import top.continew.admin.hrcommon.model.entity.NoticeDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.model.enums.*;
import top.continew.admin.hrcommon.model.resp.ApiActivityResp;
import top.continew.admin.customer.model.req.ActivityCreateReq;
import top.continew.admin.customer.model.req.ActivityPageReq;
import top.continew.admin.hrcommon.model.resp.ApiActivityDetailResp;
import top.continew.admin.customer.service.ActivityService;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端活动服务实现
 *
 * @author weilai
 * @since 2026/05/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityMemberMapper activityMemberMapper;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final NoticeMapper noticeMapper;

    @Override
    public PageResp<ApiActivityResp> page(ActivityPageReq req) {
        // 创建分页对象
        Page<ActivityDO> page = new Page<>(req.getPage(), req.getSize());

        // 构建查询条件
        QueryWrapper<ActivityDO> wrapper = new QueryWrapper<>();

        // 只查询已审核通过的活动
        wrapper.eq("ba.status", ActivityStatusEnum.APPROVED.getValue());

        // 活动类型过滤（如果传递了类型参数）
        if (req.getType() != null) {
            wrapper.eq("ba.type", req.getType());
        }

        // 处理排序：置顶优先，然后按指定字段排序
        if (StringUtils.hasText(req.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(req.getSortOrder());

            if ("startTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "ba.start_time");
            } else if ("endTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "ba.end_time");
            } else if ("createTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "ba.create_time");
            } else {
                // 默认按活动开始时间倒序
                wrapper.orderByDesc("ba.start_time");
            }
        } else {
            // 默认：置顶优先，然后按活动开始时间倒序
            wrapper.orderByDesc("ba.is_top");
            wrapper.orderByDesc("ba.start_time");
        }

        // 执行自定义分页查询，直接返回 ApiActivityResp
        IPage<ApiActivityResp> result = activityMapper.apiPage(page, wrapper);

        // 直接构建分页响应，不做对象转换
        return PageResp.build(result);
    }

    @Override
    public ApiActivityDetailResp getActivityDetail(Long id) {
        // 一次连表查询获取活动详情和成员信息（SQL已过滤：只查询已删除=0且审核状态=2的活动）
        ApiActivityDetailResp resp = activityMapper.getActivityMemberInfo(id);
        if (resp == null) {
            throw new BusinessException("活动不存在或尚未审核通过");
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ActivityCreateReq req) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 创建活动实体
        ActivityDO activity = new ActivityDO();
        activity.setTitle(req.getTitle());
        activity.setContent(req.getContent());
        activity.setImage(req.getImage());
        activity.setActivityPeopleNums(req.getEventPeopleNums());
        activity.setAttachment(req.getAttachment());
        activity.setStartTime(req.getStartTime());
        activity.setEndTime(req.getEndTime());
        // 默认为普通活动
        activity.setType(ActivityTypeEnum.NORMAL);
        // 默认为待审核状态
        activity.setStatus(ActivityStatusEnum.PENDING);
        // 默认不置顶
        activity.setIsTop(false);
        activity.setCreateUser(userId);
        // 插入活动记录
        activityMapper.insert(activity);
        log.info("用户创建活动申请成功，用户ID：{}，活动ID：{}", userId, activity.getId());

        // 构建活动成员列表（用于批量插入）
        List<ActivityMemberDO> membersToInsert = new ArrayList<>();

        // 添加创建人到参与人员表（创建人自动报名）
        ActivityMemberDO creatorMember = new ActivityMemberDO();
        creatorMember.setActivityId(activity.getId());
        creatorMember.setUserId(userId);
        // 创建人设置为创建人类型
        creatorMember.setType(ActivityMemberType.SPEAKER);
        // 已报名
        creatorMember.setStatus(1);
        creatorMember.setCreateUser(userId);
        membersToInsert.add(creatorMember);

        // 添加必须参加的人员
        if (req.getRequiredMemberIds() != null && !req.getRequiredMemberIds().isEmpty()) {
            for (Long memberId : req.getRequiredMemberIds()) {
                if (memberId != null) {
                    // 跳过创建人，避免重复插入
                    if (memberId.equals(userId)) {
                        log.info("跳过创建人，避免重复插入，活动ID：{}，用户ID：{}", activity.getId(), memberId);
                        continue;
                    }

                    ActivityMemberDO member = new ActivityMemberDO();
                    member.setActivityId(activity.getId());
                    member.setUserId(memberId);
                    // 统一设置为必参加人类型
                    member.setType(ActivityMemberType.MANDATORY);
                    // 已报名
                    member.setStatus(1);
                    member.setCreateUser(userId);
                    membersToInsert.add(member);
                }
            }
        }

        // 批量插入活动成员
        if (!membersToInsert.isEmpty()) {
            activityMemberMapper.insertBatch(membersToInsert);
            log.info("批量插入活动成员成功，活动ID：{}，成员数量：{}", activity.getId(), membersToInsert.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void participate(Long activityId) {

        Long userId = StpUtil.getLoginIdAsLong();

        // 查询活动是否存在
        ActivityDO activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在或已被删除");
        }

        // 只允许报名已审核通过的活动
        if (!ActivityStatusEnum.APPROVED.equals(activity.getStatus())) {
            throw new BusinessException("该活动尚未审核通过，无法报名");
        }
        // 检查活动人数是否已满（只统计主动参加人type=3）
        if (activity.getActivityPeopleNums() != null && activity.getActivityPeopleNums() > 0) {
            long currentCount = activityMemberMapper.selectCount(Wrappers.<ActivityMemberDO>lambdaQuery()
                .eq(ActivityMemberDO::getActivityId, activityId)
                .eq(ActivityMemberDO::getStatus, 1)
                .eq(ActivityMemberDO::getDeleted, 0)
                .eq(ActivityMemberDO::getType, ActivityMemberType.VOLUNTARY.getValue()));
            if (currentCount >= activity.getActivityPeopleNums()) {
                throw new BusinessException("抱歉，该活动报名人数已满");
            }
        }
        // 检查用户是否已经报名过该活动
        ActivityMemberDO existingMember = activityMemberMapper.selectOne(Wrappers.<ActivityMemberDO>lambdaQuery()
            .eq(ActivityMemberDO::getActivityId, activityId)
            .eq(ActivityMemberDO::getUserId, userId)
            .eq(ActivityMemberDO::getDeleted, 0));

        if (existingMember != null) {
            if (existingMember.getStatus() == 1) {
                // 已报名状态
                throw new BusinessException("您已经报名过该活动，请勿重复报名");
            } else if (existingMember.getStatus() == 2) {
                // 已取消报名，更新为已报名状态
                activityMemberMapper.update(null, Wrappers.<ActivityMemberDO>lambdaUpdate()
                    .eq(ActivityMemberDO::getActivityId, activityId)
                    .eq(ActivityMemberDO::getUserId, userId)
                    .set(ActivityMemberDO::getStatus, 1));
                log.info("用户重新报名活动成功，用户ID：{}，活动ID：{}", userId, activityId);

                // 重新报名成功后发布事件，发送钉钉消息
                sendActivityParticipateNotice(activity, userId);
                // 发送系统通知
                sendActivitySystemNotice(activity, userId);
                return;
            }
        }

        // 插入报名记录
        ActivityMemberDO member = new ActivityMemberDO();
        member.setActivityId(activityId);
        member.setUserId(userId);
        member.setType(ActivityMemberType.VOLUNTARY);
        member.setStatus(1); // 已报名
        activityMemberMapper.insert(member);

        log.info("用户报名活动成功，用户ID：{}，活动ID：{}", userId, activityId);

        // 报名成功后发布事件，发送钉钉消息
        sendActivityParticipateNotice(activity, userId);
        // 发送系统通知
        sendActivitySystemNotice(activity, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelParticipate(Long activityId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询活动是否存在
        ActivityDO activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在或已被删除");
        }

        // 检查活动是否已开始或已结束
        LocalDateTime now = LocalDateTime.now();
        if (activity.getStartTime() != null && now.isAfter(activity.getStartTime())) {
            throw new BusinessException("活动已开始或已结束，无法取消报名");
        }

        // 查询用户的报名记录
        ActivityMemberDO member = activityMemberMapper.selectOne(Wrappers.<ActivityMemberDO>lambdaQuery()
            .eq(ActivityMemberDO::getActivityId, activityId)
            .eq(ActivityMemberDO::getUserId, userId)
            .eq(ActivityMemberDO::getDeleted, 0));

        if (member == null) {
            throw new BusinessException("您未报名该活动，无法取消");
        }

        if (member.getStatus() == 2) {
            throw new BusinessException("您已经取消过该活动的报名");
        }

        // 更新状态为已取消
        member.setStatus(2);
        activityMemberMapper.updateById(member);

        log.info("用户取消活动报名成功，用户ID：{}，活动ID：{}", userId, activityId);
    }

    @Override
    public java.util.List<ApiActivityResp> listPinnedActivities() {
        // 构建查询条件
        QueryWrapper<ActivityDO> wrapper = new QueryWrapper<>();

        // 只查询已审核通过的活动
        wrapper.eq("ba.status", ActivityStatusEnum.APPROVED.getValue());

        // 只查询置顶的活动
        wrapper.eq("ba.is_top", true);

        // 按活动开始时间倒序
        wrapper.orderByDesc("ba.start_time");

        // 创建分页对象，设置一个较大的页大小来获取所有置顶活动
        Page<ActivityDO> page = new Page<>(1, 100);

        // 执行查询
        IPage<ApiActivityResp> result = activityMapper.apiPage(page, wrapper);

        // 返回记录列表
        return result.getRecords();
    }

    @Override
    public java.util.List<ApiActivityResp> listRecentParticipatedActivities() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询用户最近参与的5条活动
        return activityMapper.listRecentParticipatedActivities(userId);
    }

    /**
     * 发送活动报名成功的通知
     *
     * @param activity 活动信息
     * @param userId   用户ID
     */
    private void sendActivityParticipateNotice(ActivityDO activity, Long userId) {
        try {
            // 获取用户信息
            //获取全部需要接收消息的人
            List<UserDO> list = userMapper.selectRequirePushMessageUserList();

            UserDO user = userMapper.selectById(userId);
            // 统计当前活动的已参加人数（只统计主动参加人type=3）
            long currentCount = activityMemberMapper.selectCount(Wrappers.<ActivityMemberDO>lambdaQuery()
                .eq(ActivityMemberDO::getActivityId, activity.getId())
                .eq(ActivityMemberDO::getStatus, 1)
                .eq(ActivityMemberDO::getDeleted, 0)
                .eq(ActivityMemberDO::getType, ActivityMemberType.VOLUNTARY.getValue()));

            // 构建消息内容
            String content = String.format("【活动报名成功通知】\n用户：%s\n活动：%s\n当前已报名人数：%d人\n恭喜您成功报名参加该活动！当前时间 %s", user
                .getNickname(), activity.getTitle(), currentCount, LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // 发布消息事件，使用现有的 SendMessageEventListener 处理发送
            eventPublisher.publishEvent(new SendMessageEvent(this, list, content, false));
            log.info("活动报名通知事件发布成功，用户ID：{}，活动ID：{}", userId, activity.getId());

        } catch (Exception e) {
            // 发送消息失败不影响业务流程
            log.error("发送活动报名通知失败，用户ID：{}，活动ID：{}，错误信息：{}", userId, activity.getId(), e.getMessage(), e);
        }
    }

    /**
     * 发送活动报名成功的系统通知
     *
     * @param activity 活动信息
     * @param userId   用户ID
     */
    private void sendActivitySystemNotice(ActivityDO activity, Long userId) {
        try {
            // 获取用户信息
            UserDO user = userMapper.selectById(userId);
            if (user == null) {
                log.warn("用户不存在，无法发送系统通知，用户ID：{}", userId);
                return;
            }

            // 构建消息内容
            String content = String.format("恭喜您成功报名参加活动！\n活动名称：%s\n报名时间：%s", activity.getTitle(), LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // 创建系统消息请求
            NoticeDO noticeDO = new NoticeDO();
            noticeDO.setTitle("报名成功");
            noticeDO.setContent(content);
            noticeDO.setType("1");
            noticeDO.setNoticeScope(NoticeScopeEnum.USER);
            noticeDO.setNoticeUsers(List.of(userId.toString()));
            noticeDO.setNoticeMethods(List.of(1));
            noticeDO.setStatus(NoticeStatusEnum.PUBLISHED);
            noticeDO.setPublishTime(LocalDateTime.now());
            // 发送系统消息
            noticeMapper.insert(noticeDO);
            log.info("活动报名系统通知发送成功，用户ID：{}，活动ID：{}", userId, activity.getId());

        } catch (Exception e) {
            // 发送系统通知失败不影响业务流程
            log.error("发送活动报名系统通知失败，用户ID：{}，活动ID：{}，错误信息：{}", userId, activity.getId(), e.getMessage(), e);
        }
    }

}
