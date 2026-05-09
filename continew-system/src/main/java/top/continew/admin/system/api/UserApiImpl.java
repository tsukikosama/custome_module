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

package top.continew.admin.system.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.system.UserInfo;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.common.api.system.UserApi;
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.system.model.req.user.UserPasswordResetReq;
import top.continew.admin.system.service.UserService;

/**
 * 用户业务 API 实现
 *
 * @author Charles7c
 * @since 2025/7/23 20:57
 */
@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserService baseService;
    private final UserMapper baseMapper;

    @Override
    @Cached(key = "#id", name = CacheConstants.USER_KEY_PREFIX, cacheType = CacheType.BOTH, syncLocal = true)
    public String getNicknameById(Long id) {
        return baseMapper.selectNicknameById(id);
    }

    @Override
    public void resetPassword(String newPassword, Long id) {
        UserPasswordResetReq req = new UserPasswordResetReq();
        req.setNewPassword(newPassword);
        baseService.resetPassword(req, id);
    }

    @Override
    public UserInfo getById(Long id) {
        UserDO userDO = baseMapper.selectById(id);
        if (userDO == null) {
            return null;
        }
        return BeanUtil.copyProperties(userDO, UserInfo.class);
    }

    @Override
    public UserInfo getByUsername(String username) {
        UserDO userDO = baseMapper.selectByUsername(username);
        if (userDO == null) {
            return null;
        }
        return BeanUtil.copyProperties(userDO, UserInfo.class);
    }

    @Override
    public UserInfo getByPhone(String phone) {
        UserDO userDO = baseMapper.selectByPhone(phone);
        if (userDO == null) {
            return null;
        }
        return BeanUtil.copyProperties(userDO, UserInfo.class);
    }

    @Override
    public UserInfo getByEmail(String email) {
        UserDO userDO = baseMapper.selectByEmail(email);
        if (userDO == null) {
            return null;
        }
        return BeanUtil.copyProperties(userDO, UserInfo.class);
    }

    @Override
    public void changePoints(Long userId, Integer points) {
        UserDO user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        Integer currentPoints = user.getPoints();
        if (currentPoints == null) {
            currentPoints = 0;
        }

        Integer newPoints = currentPoints + points;
        if (newPoints < 0) {
            throw new RuntimeException("积分不足");
        }

        user.setPoints(newPoints);
        baseMapper.updateById(user);
    }
}
