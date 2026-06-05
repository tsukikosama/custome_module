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

package top.continew.admin.auth.handler;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.continew.admin.auth.AbstractLoginHandler;
import top.continew.admin.auth.enums.AuthTypeEnum;
import top.continew.admin.auth.model.req.DingTalkLoginReq;
import top.continew.admin.auth.model.resp.LoginResp;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.model.entity.user.UserDO;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.starter.core.util.validation.ValidationUtils;

/**
 * 钉钉登录处理器
 *
 * @author Continew
 */
@Component
@RequiredArgsConstructor
public class DingTalkLoginHandler extends AbstractLoginHandler<DingTalkLoginReq> {

    private final DingTalkApiService dingTalkApiService;

    @Override
    public LoginResp login(DingTalkLoginReq req, ClientResp client, HttpServletRequest request) {
        // 通过授权码获取用户信息
        ResponseEntity<JSONObject> userInfoResponse = dingTalkApiService.getUsers(req.getAuthCode());

        // 检查获取用户信息是否成功
        ValidationUtils.throwIfNull(userInfoResponse, "获取钉钉用户信息失败");
        ValidationUtils.throwIf(!userInfoResponse.getStatusCode().is2xxSuccessful(), "获取钉钉用户信息失败");

        JSONObject userInfo = userInfoResponse.getBody();
        ValidationUtils.throwIfNull(userInfo, "钉钉用户信息为空");

        // 获取用户手机号
        String phone = userInfo.getString("phone");
        ValidationUtils.throwIfBlank(phone, "无法获取钉钉用户手机号");

        // 根据手机号查询用户
        UserDO user = userService.getByPhone(phone);
        ValidationUtils.throwIfNull(user, "手机号未注册，请联系管理员");
        //校验用户是否被禁用
        ValidationUtils.throwIfEqual(user.getStatus(), 2, "该用户已被禁用，请联系管理员");
        // 检查用户状态
        super.checkUserStatus(user);

        // 更新请求中的用户信息
        req.setPhone(phone);
        req.setNickname(userInfo.getString("nick"));
        req.setUnionId(userInfo.getString("unionId"));

        // 执行认证
        return super.authenticate(user, client);
    }

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.DINGTALK;
    }
}