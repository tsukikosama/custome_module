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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.PasswordUpdateReq;
import top.continew.admin.customer.model.resp.UserInfoResp;
import top.continew.admin.customer.model.resp.UserRecord;
import top.continew.admin.customer.model.resp.UserStatResp;
import top.continew.admin.customer.service.UserService;
import top.continew.starter.log.annotation.Log;

import java.util.List;

/**
 * 用户 API
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Tag(name = "用户 API")
@Log(module = "用户管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiUserController {

    private final UserService userService;

    @Operation(summary = "查询用户个人信息", description = "查询当前登录用户的个人信息")
    @GetMapping("/user/info")
    public UserInfoResp getInfo() {
        return userService.getInfo();
    }

    @Operation(summary = "查询用户统计信息", description = "查询当前登录用户的统计数据")
    @GetMapping("/stat")
    public UserStatResp getStat() {
        return userService.getStat();
    }

    @Operation(summary = "修改密码", description = "用户修改密码，需要验证旧密码")
    @PostMapping("/password/update")
    public void updatePassword(@Validated @RequestBody PasswordUpdateReq passwordUpdateReq) {
        userService.updatePassword(passwordUpdateReq);
    }

    @Operation(summary = "查询所有在职用户", description = "查询所有在职用户的ID和昵称列表")
    @GetMapping("/users/all")
    public List<UserRecord> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }
}
