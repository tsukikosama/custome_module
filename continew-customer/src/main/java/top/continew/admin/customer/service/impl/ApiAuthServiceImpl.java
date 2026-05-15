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
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.enums.DisEnableStatusEnum;
import top.continew.admin.customer.model.req.ApiDingTalkLoginReq;
import top.continew.admin.customer.model.req.ApiLoginReq;
import top.continew.admin.customer.model.resp.ApiUserInfoResp;
import top.continew.admin.customer.service.ApiAuthService;
import top.continew.admin.hrcommon.mapper.dept.DeptMapper;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.dept.DeptDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.starter.core.util.validation.CheckUtils;

/**
 * 客户端认证业务实现
 *
 * @author weilai
 * @since 2026/01/15
 */
@Service
@RequiredArgsConstructor
public class ApiAuthServiceImpl implements ApiAuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DeptMapper deptMapper;
    private final DingTalkApiService dingTalkApiService;

    @Override
    public String login(ApiLoginReq req) {
        // 根据手机号查询用户（selectByPhone 方法会自动加密手机号）
        UserDO user = userMapper.selectByPhone(req.getPhone());
        CheckUtils.throwIfNull(user, "手机号或密码错误");

        // 检查用户状态
        CheckUtils.throwIf(DisEnableStatusEnum.DISABLE.equals(user.getStatus()), "此账号已被禁用，如有疑问，请联系管理员");

        // 检查部门状态
        if (user.getDeptId() != null) {
            DeptDO dept = deptMapper.selectById(user.getDeptId());
            if (dept != null && DisEnableStatusEnum.DISABLE.equals(dept.getStatus())) {
                CheckUtils.throwIf(true, "此账号所属部门已被禁用，如有疑问，请联系管理员");
            }
        }

        // 验证密码（客户端传明文密码）
        CheckUtils.throwIf(!passwordEncoder.matches(req.getPassword(), user.getPassword()), "手机号或密码错误");

        // 登录（使用 Sa-Token）
        StpUtil.login(user.getId());

        // 返回Token
        return StpUtil.getTokenValue();
    }

    @Override
    public String dingTalkLogin(ApiDingTalkLoginReq req) {
        // 通过授权码获取钉钉用户信息
        ResponseEntity<JSONObject> userInfoResponse = dingTalkApiService.getUsers(req.getAuthCode());

        // 检查获取用户信息是否成功
        CheckUtils.throwIfNull(userInfoResponse, "获取钉钉用户信息失败");
        CheckUtils.throwIf(!userInfoResponse.getStatusCode().is2xxSuccessful(), "获取钉钉用户信息失败");

        JSONObject userInfo = userInfoResponse.getBody();
        CheckUtils.throwIfNull(userInfo, "钉钉用户信息为空");

        // 获取用户手机号
        String phone = userInfo.getString("phone");
        CheckUtils.throwIfBlank(phone, "无法获取钉钉用户手机号");

        // 根据手机号查询用户（selectByPhone 方法会自动加密手机号）
        UserDO user = userMapper.selectByPhone(phone);
        CheckUtils.throwIfNull(user, "手机号未注册，请联系管理员");

        // 检查用户状态
        CheckUtils.throwIf(DisEnableStatusEnum.DISABLE.equals(user.getStatus()), "此账号已被禁用，如有疑问，请联系管理员");

        // 检查部门状态
        if (user.getDeptId() != null) {
            DeptDO dept = deptMapper.selectById(user.getDeptId());
            if (dept != null && DisEnableStatusEnum.DISABLE.equals(dept.getStatus())) {
                CheckUtils.throwIf(true, "此账号所属部门已被禁用，如有疑问，请联系管理员");
            }
        }

        // 登录（使用 Sa-Token）
        StpUtil.login(user.getId());

        // 返回Token
        return StpUtil.getTokenValue();
    }

    @Override
    public ApiUserInfoResp getUserInfo() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询用户信息
        UserDO user = userMapper.selectById(userId);
        CheckUtils.throwIfNull(user, "用户不存在");

        // 检查部门状态并查询部门信息
        if (user.getDeptId() != null) {
            DeptDO dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) {
                CheckUtils.throwIf(DisEnableStatusEnum.DISABLE.equals(dept.getStatus()), "此账号所属部门已被禁用，如有疑问，请联系管理员");
            }
        }

        // 组装返回数据
        ApiUserInfoResp resp = new ApiUserInfoResp();
        BeanUtils.copyProperties(user, resp);

        // 性别枚举转换为整数值
        if (user.getGender() != null) {
            resp.setGender(user.getGender().getValue());
        }
        // 查询并设置部门信息
        if (user.getDeptId() != null) {
            DeptDO dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) {
                resp.setDeptId(dept.getId());
                resp.setDeptName(dept.getName());
            }
        }
        return resp;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }
}
