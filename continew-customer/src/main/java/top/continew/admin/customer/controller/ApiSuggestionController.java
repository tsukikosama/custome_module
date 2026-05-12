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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.SuggestionCreateReq;
import top.continew.admin.customer.model.req.SuggestionPageReq;
import top.continew.admin.hrcommon.model.resp.ApiSuggestionResp;
import top.continew.admin.customer.service.ApiSuggestionService;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 建议管理 API
 *
 * @author weilai
 * @since 2026/05/08
 */
@Tag(name = "建议管理 API")
@Log(module = "建议管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suggestion")
public class ApiSuggestionController {

    private final ApiSuggestionService apiSuggestionService;

    @Operation(summary = "提交建议", description = "用户提交建议或反馈")
    @PostMapping
    public void create(@RequestBody @Valid SuggestionCreateReq req) {
        apiSuggestionService.create(req);
    }

    @SaIgnore
    @Operation(summary = "建议列表分页查询", description = "分页查询当前登录用户的建议列表")
    @GetMapping
    public PageResp<ApiSuggestionResp> apiPage(@Valid SuggestionPageReq req) {
        return apiSuggestionService.apiPage(req);
    }
}
