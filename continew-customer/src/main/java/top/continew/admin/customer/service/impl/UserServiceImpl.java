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
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.customer.model.req.PasswordUpdateReq;
import top.continew.admin.customer.model.resp.UserInfoResp;
import top.continew.admin.customer.model.resp.UserRecord;
import top.continew.admin.customer.model.resp.UserStatResp;
import top.continew.admin.customer.service.UserService;
import top.continew.admin.common.mapper.user.UserMapper;



import java.util.List;

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

    @Override
    public UserInfoResp getInfo() {
        return null;
    }

    @Override
    public UserStatResp getStat() {
        return null;
    }

    @Override
    public void updatePassword(PasswordUpdateReq passwordUpdateReq) {

    }

    @Override
    public List<UserRecord> getAllActiveUsers() {
        return List.of();
    }
}
