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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.continew.admin.auth.model.resp.UserInfoResp;
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.customer.model.req.PasswordUpdateReq;
import top.continew.admin.customer.model.resp.UserRecord;
import top.continew.admin.customer.model.resp.UserStatResp;
import top.continew.admin.customer.service.UserService;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.mapper.ActivityMemberMapper;
import top.continew.admin.hrcommon.model.resp.UserActivityStatResp;
import top.continew.starter.core.util.validation.CheckUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
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
    private final ActivityMemberMapper activityMemberMapper;

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

        // 使用 XML mapper 一次性统计用户参与的活动
        UserActivityStatResp statResp = activityMemberMapper.statUserActivities(userId);

        if (statResp != null) {
            resp.setActivityCount(statResp.getActivityCount());

            // 将逗号分隔的字符串转换为 List<Long>
            if (StringUtils.isNotBlank(statResp.getActivityIds())) {
                List<Long> activityIds = Arrays.stream(statResp.getActivityIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                resp.setActivityIds(activityIds);
            } else {
                resp.setActivityIds(Collections.emptyList());
            }
        } else {
            resp.setActivityCount(0);
            resp.setActivityIds(Collections.emptyList());
        }

        return resp;
    }

    @Override
    public void updatePassword(PasswordUpdateReq passwordUpdateReq) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 查询用户信息
        UserDO userDO = userMapper.selectById(userId);
        CheckUtils.throwIfNull(userDO, "用户不存在");

        // 2. 验证旧密码是否正确
        CheckUtils.throwIf(!passwordEncoder.matches(passwordUpdateReq.getOldPassword(), userDO
            .getPassword()), "旧密码不正确");

        // 3. 验证新密码不能与旧密码相同
        CheckUtils.throwIfEqual(passwordUpdateReq.getOldPassword(), passwordUpdateReq.getNewPassword(), "新密码不能与旧密码相同");

        // 4. 更新用户密码（使用 PasswordEncoder 加密）
        userDO.setPassword(passwordUpdateReq.getNewPassword());
        userMapper.updateById(userDO);

        log.info("用户 {} ({}) 密码已修改", userDO.getNickname(), userDO.getUsername());
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
