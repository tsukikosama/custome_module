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
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.continew.admin.auth.model.resp.UserInfoResp;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.api.dingDingApi.request.SendMessageReq;
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.customer.model.resp.UserRecord;
import top.continew.admin.customer.model.resp.UserStatResp;
import top.continew.admin.customer.service.UserService;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.starter.core.util.validation.CheckUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户API业务实现
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DingTalkApiService dingTalkApiService;

    // TODO: 注入 ActivityMemberMapper
    // private final ActivityMemberMapper activityMemberMapper;

    @Override
    public UserInfoResp getInfo() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询用户信息
        UserDO userDO = userMapper.selectById(userId);
        CheckUtils.throwIfNull(userDO, "用户不存在");

        UserInfoResp resp = new UserInfoResp();
        resp.setId(userDO.getId());
        resp.setUsername(userDO.getUsername());
        resp.setNickname(userDO.getNickname());
        resp.setAvatar(userDO.getAvatar());
        resp.setPhone(userDO.getPhone());
        resp.setEmail(userDO.getEmail());
        resp.setPoints(userDO.getPoints());
        // TODO: 查询部门名称
        resp.setDeptName("");

        return resp;
    }

    @Override
    public UserStatResp getStat() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        UserStatResp resp = new UserStatResp();

        // TODO: 统计报名活动数量（状态为已报名的活动）
        // List<ActivityMemberDO> activityMembers = activityMemberMapper.selectList(
        //     Wrappers.<ActivityMemberDO>lambdaQuery()
        //         .eq(ActivityMemberDO::getUserId, userId)
        //         .eq(ActivityMemberDO::getStatus, 1) // 已报名
        // );
        // resp.setActivityCount(activityMembers.size());
        // resp.setActivityIds(activityMembers.stream().map(ActivityMemberDO::getActivityId).collect(Collectors.toList()));

        // TODO: 统计问卷数量（暂时返回0）
        resp.setActivityCount(0);
        resp.setActivityIds(List.of());
        resp.setQuestionnaireCount(0);
        resp.setQuestionnaireIds(List.of());

        return resp;
    }

    @Override
    public void resetPassword() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 生成新密码（这里简化为固定密码，实际应该生成随机密码）
        String newPassword = "123456";

        // 2. 更新用户密码
        UserDO userDO = userMapper.selectById(userId);
        CheckUtils.throwIfNull(userDO, "用户不存在");

        // 不使用 PasswordEncoder 加密，直接存储明文密码
        // 系统会在登录时使用 passwordEncoder.matches() 进行验证
        userDO.setPassword(newPassword);
        userMapper.updateById(userDO);

        log.info("用户 {} ({}) 密码已重置，新密码：{}", userDO.getNickname(), userDO.getUsername(), newPassword);

        // 3. 通过钉钉发送新密码给用户
        sendPasswordToDingTalk(userDO, newPassword);
    }

    /**
     * 通过钉钉发送新密码给用户
     *
     * @param userDO      用户信息
     * @param newPassword 新密码
     */
    private void sendPasswordToDingTalk(UserDO userDO, String newPassword) {
        try {
            // 检查用户是否有钉钉ID
            if (userDO.getDingdingId() == null || userDO.getDingdingId().isEmpty()) {
                log.warn("用户 {} ({}) 没有绑定钉钉账号，无法发送密码重置消息", userDO.getNickname(), userDO.getUsername());
                return;
            }

            // 构建发送消息请求
            SendMessageReq sendMessageReq = new SendMessageReq();
            sendMessageReq.setUseridList(userDO.getDingdingId());

            // 构建消息内容
            SendMessageReq.Msg msg = new SendMessageReq.Msg();
            msg.setMsgType("text");

            SendMessageReq.Text text = new SendMessageReq.Text();
            text.setContent(String.format("【密码重置通知】您好 %s，您已成功重置密码。新密码：%s，请妥善保管。如非本人操作，请及时联系管理员。", userDO
                .getNickname(), newPassword));
            msg.setText(text);

            sendMessageReq.setMsg(msg);

            // 发送消息
            com.alibaba.fastjson2.JSONObject response = dingTalkApiService.sendConversationMessage(sendMessageReq);

            // 检查发送结果
            if (response != null && response.getInteger("errcode") != null && response.getInteger("errcode") == 0) {
                log.info("密码重置消息已成功发送给用户 {}", userDO.getNickname());
            } else {
                log.error("发送密码重置消息失败：{}", response != null ? response.toJSONString() : "响应为空");
            }
        } catch (Exception e) {
            log.error("发送密码重置消息时发生异常：", e);
        }
    }

    @Override
    @Cached(name = CacheConstants.USER_LIST_KEY_PREFIX, key = "'ALL_ACTIVE_USERS'", cacheType = CacheType.BOTH, syncLocal = true, expire = 30, timeUnit = TimeUnit.MINUTES)
    public List<UserRecord> getAllActiveUsers() {
        // 查询所有启用状态的用户
        List<UserDO> userList = userMapper.selectList(Wrappers.<UserDO>lambdaQuery()
            .eq(UserDO::getStatus, top.continew.admin.common.enums.DisEnableStatusEnum.ENABLE)
            .eq(UserDO::getDeleted, false));

        // 转换为 UserRecord
        return userList.stream().map(userDO -> {
            UserRecord resp = new UserRecord();
            resp.setId(userDO.getId());
            resp.setNickname(userDO.getNickname());
            return resp;
        }).collect(Collectors.toList());
    }
}
