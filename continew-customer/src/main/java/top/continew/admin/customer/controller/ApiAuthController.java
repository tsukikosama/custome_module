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

package top.continew.admin.customer.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.ApiDingTalkLoginReq;
import top.continew.admin.customer.model.req.ApiLoginReq;
import top.continew.admin.customer.model.resp.ApiUserInfoResp;
import top.continew.admin.customer.service.ApiAuthService;
import top.continew.starter.log.annotation.Log;

/**
 * 认证 API
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Tag(name = "认证 API")
@Log(module = "认证管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthService apiAuthService;

    @SaIgnore
    @Operation(summary = "登录", description = "手机号密码登录")
    @PostMapping("/login")
    public String login(@RequestBody @Validated ApiLoginReq req) {
        return apiAuthService.login(req);
    }

    @SaIgnore
    @Operation(summary = "钉钉登录", description = "钉钉扫码登录")
    @PostMapping("/dingtalk/login")
    public String dingTalkLogin(@RequestBody @Validated ApiDingTalkLoginReq req) {
        return apiAuthService.dingTalkLogin(req);
    }

    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/user/info")
    public ApiUserInfoResp getUserInfo() {
        return apiAuthService.getUserInfo();
    }

    @Operation(summary = "登出", description = "退出登录")
    @PostMapping("/logout")
    public void logout() {
        apiAuthService.logout();
    }
}
