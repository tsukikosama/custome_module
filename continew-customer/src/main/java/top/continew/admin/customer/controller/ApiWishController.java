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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.WishCreateReq;
import top.continew.admin.customer.model.req.WishPageReq;
import top.continew.admin.customer.model.resp.ApiWishResp;
import top.continew.admin.customer.service.WishService;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 心愿 API
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Tag(name = "心愿 API")
@Log(module = "心愿管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class ApiWishController {

    private final WishService wishService;

    @Operation(summary = "新增心愿", description = "用户提交心愿建议")
    @PostMapping("/add")
    public void create(@RequestBody @Valid WishCreateReq req) {
        wishService.create(req);
    }

    @Operation(summary = "分页查询我的心愿", description = "分页查询当前登录用户提交的心愿记录")
    @GetMapping
    public PageResp<ApiWishResp> customPage(@Valid WishPageReq req) {
        return wishService.customPage(req);
    }

    @Operation(summary = "分页查询全部心愿", description = "分页查询所有用户提交的心愿记录，供用户选择支持")
    @GetMapping("/all")
    public PageResp<ApiWishResp> allWish(@Valid WishPageReq req) {
        return wishService.allWish(req);
    }
}
