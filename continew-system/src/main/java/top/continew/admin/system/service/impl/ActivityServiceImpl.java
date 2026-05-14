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

package top.continew.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.controller.biz.model.entity.ActivityMemberDO;
import top.continew.admin.hrcommon.mapper.ActivityMapper;
import top.continew.admin.hrcommon.mapper.ActivityMemberMapper;
import top.continew.admin.hrcommon.model.entity.ActivityDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.model.enums.ActivityMemberType;
import top.continew.admin.hrcommon.model.enums.ActivityStatusEnum;
import top.continew.admin.hrcommon.model.enums.NoticeScopeEnum;
import top.continew.admin.hrcommon.model.enums.NoticeStatusEnum;
import top.continew.admin.hrcommon.model.resp.ActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityResp;
import top.continew.admin.hrcommon.model.req.NoticeReq;
import top.continew.admin.system.event.SendBroadcastMessageEvent;
import top.continew.admin.system.event.SendMessageEvent;
import top.continew.admin.system.model.query.ActivityQuery;
import top.continew.admin.system.model.req.ActivityReq;
import top.continew.admin.system.model.req.ActivityReviewReq;
import top.continew.admin.system.service.ActivityService;
import top.continew.admin.system.service.NoticeService;
import top.continew.admin.system.service.UserService;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.query.SortQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 活动业务实现
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl extends BaseServiceImpl<ActivityMapper, ActivityDO, ActivityResp, ActivityDetailResp, ActivityQuery, ActivityReq> implements ActivityService {

    private final ActivityMemberMapper activityMemberMapper;
    private final NoticeService noticeService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PageResp<ActivityResp> page(ActivityQuery query, PageQuery pageQuery) {

        QueryWrapper<ActivityDO> queryWrapper = this.buildQueryWrapper(query);
        this.sort(queryWrapper, pageQuery);
        queryWrapper.groupBy("ba.id");
        IPage<ActivityResp> page = this.baseMapper.customPage(new Page((long)pageQuery.getPage(), (long)pageQuery
            .getSize()), queryWrapper);
        PageResp<ActivityResp> pageResp = PageResp.build(page, this.getListClass());
        pageResp.getList().forEach(this::fill);
        return pageResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void afterCreate(ActivityReq req, ActivityDO entity) {
        super.afterCreate(req, entity);

        // 活动创建后，插入活动参与人员记录
        insertActivityMembers(entity.getId(), req);
    }

    @Override
    public ActivityDetailResp get(Long id) {
        ActivityDetailResp activityDetailResp = baseMapper.getDetailById(id);
        CheckUtils.throwIfNull(activityDetailResp, "活动不存在");
        return activityDetailResp;
    }

    /**
     * 插入活动参与人员
     *
     * @param activityId 活动ID
     * @param req        活动请求参数
     */
    private void insertActivityMembers(Long activityId, ActivityReq req) {
        try {
            // 1. 插入演讲人记录
            if (req.getSpeakerUserId() != null) {
                ActivityMemberDO speaker = new ActivityMemberDO();
                speaker.setActivityId(activityId);
                speaker.setUserId(req.getSpeakerUserId());
                speaker.setType(ActivityMemberType.SPEAKER);
                // 已报名
                speaker.setStatus(1);
                activityMemberMapper.insert(speaker);
                log.info("插入活动演讲人记录成功，活动ID：{}，用户ID：{}", activityId, req.getSpeakerUserId());
            }

            // 2. 插入必参加人记录（逗号分隔的用户ID）
            if (req.getRequireUserId() != null && !req.getRequireUserId().trim().isEmpty()) {
                List<String> requireUserIds = Arrays.stream(req.getRequireUserId().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

                for (String userIdStr : requireUserIds) {
                    try {
                        Long userId = Long.parseLong(userIdStr);

                        // 检查是否已经存在该用户的记录
                        ActivityMemberDO existingMember = activityMemberMapper.selectOne(Wrappers
                            .<ActivityMemberDO>lambdaQuery()
                            .eq(ActivityMemberDO::getActivityId, activityId)
                            .eq(ActivityMemberDO::getUserId, userId)
                            .eq(ActivityMemberDO::getDeleted, 0));

                        if (existingMember == null) {
                            // 不存在记录，插入新记录
                            ActivityMemberDO member = new ActivityMemberDO();
                            member.setActivityId(activityId);
                            member.setUserId(userId);
                            member.setType(ActivityMemberType.MANDATORY);
                            // 已报名
                            member.setStatus(1);
                            activityMemberMapper.insert(member);
                            log.info("插入活动必参加人记录成功，活动ID：{}，用户ID：{}", activityId, userId);
                        } else if (existingMember.getStatus() == 2) {
                            // 用户已取消报名，更新状态为已报名
                            activityMemberMapper.update(null, Wrappers.<ActivityMemberDO>lambdaUpdate()
                                .eq(ActivityMemberDO::getActivityId, activityId)
                                .eq(ActivityMemberDO::getUserId, userId)
                                .set(ActivityMemberDO::getStatus, 1));
                            log.info("用户重新报名成功，活动ID：{}，用户ID：{}", activityId, userId);
                        } else {
                            log.warn("该用户已存在活动参与记录，活动ID：{}，用户ID：{}，跳过插入", activityId, userId);
                        }
                    } catch (NumberFormatException e) {
                        log.error("解析用户ID失败：{}，跳过该用户", userIdStr, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("插入活动参与人员记录失败，活动ID：{}", activityId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void review(ActivityReviewReq req) {
        // 查询活动是否存在
        ActivityDO activity = baseMapper.selectById(req.getId());
        CheckUtils.throwIfNull(activity, "活动不存在");

        // 检查活动状态是否为待审核
        CheckUtils.throwIfNotEqual(ActivityStatusEnum.PENDING, activity.getStatus(), "只有待审核状态的活动才能审核");

        // 检查审核结果是否为审核成功或审核失败
        CheckUtils.throwIf(!ActivityStatusEnum.APPROVED.equals(req.getStatus()) && !ActivityStatusEnum.FAILED.equals(req
            .getStatus()), "审核结果只能是审核成功或审核失败");

        // 使用 updateWrapper 更新活动审核信息
        int rows = this.baseMapper.update(null, Wrappers.<ActivityDO>lambdaUpdate()
            .eq(ActivityDO::getId, req.getId())
            .set(ActivityDO::getStatus, req.getStatus())
            .set(ActivityDO::getAuditRemark, req.getAuditRemark())
            .set(ActivityDO::getAuditTime, LocalDateTime.now())
            .set(ActivityDO::getAuditUser, UserContextHolder.getUserId()));

        CheckUtils.throwIf(rows == 0, "审核失败");

        log.info("活动审核成功，活动ID：{}，审核结果：{}", req.getId(), req.getStatus().getDescription());

        // 如果审核通过，给所有用户发送通知
        if (ActivityStatusEnum.APPROVED.equals(req.getStatus())) {
            sendAuditSuccessNotice(activity);

            // 给必参加人员发送报名成功通知
            try {
                List<ActivityMemberDO> mandatoryMembers = activityMemberMapper.selectList(Wrappers
                    .<ActivityMemberDO>lambdaQuery()
                    .eq(ActivityMemberDO::getActivityId, activity.getId())
                    .eq(ActivityMemberDO::getType, ActivityMemberType.MANDATORY)
                    .eq(ActivityMemberDO::getStatus, 1)
                    .eq(ActivityMemberDO::getDeleted, 0));
                if (!mandatoryMembers.isEmpty()) {
                    List<Long> userIds = mandatoryMembers.stream().map(ActivityMemberDO::getUserId).toList();
                    List<UserDO> users = userService.listByIds(userIds);
                    if (!users.isEmpty()) {
                        String content = String.format(
                            "【活动报名成功通知】活动「%s」已审核通过，您已被添加为必参加人员\n活动时间：%s ~ %s",
                            activity.getTitle(),
                            activity.getStartTime() != null ? activity.getStartTime().format(DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm:ss")) : "待定",
                            activity.getEndTime() != null ? activity.getEndTime().format(DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm:ss")) : "待定");
                        eventPublisher.publishEvent(new SendMessageEvent(this, users, content, false));
                        log.info("审核通过必参加人员通知发送成功，活动ID：{}，通知人数：{}", activity.getId(), users.size());
                    }
                }
            } catch (Exception e) {
                log.error("发送审核通过必参加人员通知失败，活动ID：{}", activity.getId(), e);
            }
        } else if (ActivityStatusEnum.FAILED.equals(req.getStatus())) {
            // 如果审核失败，给活动创建者发送通知
            sendAuditFailureNotice(activity, req.getAuditRemark());
        }
    }

    /**
     * 发送审核通过通知
     *
     * @param activity 活动信息
     */
    private void sendAuditSuccessNotice(ActivityDO activity) {
        try {
            // 构建通知内容
            String content = String.format("新活动「%s」已发布，活动时间：%s 至 %s，欢迎参加！", activity.getTitle(), activity
                .getStartTime(), activity.getEndTime());

            // 创建系统通知
            NoticeReq noticeReq = new NoticeReq();
            noticeReq.setTitle("新活动通知");
            noticeReq.setContent(content);
            noticeReq.setStatus(NoticeStatusEnum.PUBLISHED);
            noticeReq.setType("1");
            // 发送给所有用户
            noticeReq.setNoticeScope(NoticeScopeEnum.ALL);
            noticeReq.setNoticeMethods(List.of(1));
            noticeReq.setIsTiming(false);

            noticeService.create(noticeReq);
            log.info("活动审核通过系统通知创建成功，活动ID：{}", activity.getId());

            // 发送钉钉广播消息给全体用户
            eventPublisher.publishEvent(new SendBroadcastMessageEvent(this, content,false));
            log.info("活动审核通过钉钉广播消息发送成功，活动ID：{}", activity.getId());
        } catch (Exception e) {
            // 记录错误日志，但不影响审核流程
            log.error("发送活动审核通过通知失败，活动ID：{}", activity.getId(), e);
        }
    }

    /**
     * 发送审核失败通知
     *
     * @param activity    活动信息
     * @param auditRemark 审核备注
     */
    private void sendAuditFailureNotice(ActivityDO activity, String auditRemark) {
        try {
            // 获取活动创建者信息
            UserDO creator = userService.getById(activity.getCreateUser());
            if (creator == null) {
                log.warn("活动创建者不存在，用户ID：{}", activity.getCreateUser());
                return;
            }

            // 创建通知
            NoticeReq noticeReq = new NoticeReq();
            noticeReq.setTitle("活动审核失败通知");
            noticeReq.setContent(String.format("您创建的活动「%s」审核未通过。%s", activity
                .getTitle(), auditRemark != null && !auditRemark.trim().isEmpty()
                    ? "失败原因：" + auditRemark
                    : "请重新提交活动申请。"));
            noticeReq.setStatus(NoticeStatusEnum.PUBLISHED);
            noticeReq.setType("1");
            noticeReq.setNoticeScope(NoticeScopeEnum.USER);
            noticeReq.setNoticeMethods(List.of(1));
            noticeReq.setNoticeUsers(List.of(creator.getId().toString()));
            noticeReq.setIsTiming(false);

            noticeService.create(noticeReq);
            log.info("活动审核失败通知发送成功，活动ID：{}，用户ID：{}", activity.getId(), creator.getId());
        } catch (Exception e) {
            // 记录错误日志，但不影响审核流程
            log.error("发送活动审核失败通知失败，活动ID：{}", activity.getId(), e);
        }
    }

    @Override
    public void export(ActivityQuery query, SortQuery sortQuery, HttpServletResponse response) {
        super.export(query, sortQuery, response);
    }
}