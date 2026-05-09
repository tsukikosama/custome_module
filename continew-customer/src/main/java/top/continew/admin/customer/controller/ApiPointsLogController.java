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
import top.continew.admin.customer.model.req.PointsLogPageReq;
import top.continew.admin.customer.model.resp.ApiPointsLogResp;
import top.continew.admin.customer.service.PointsLogService;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 积分日志 API
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Tag(name = "积分日志 API")
@Log(module = "积分日志管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pointLog")
public class ApiPointsLogController {

    private final PointsLogService pointsLogService;

    @Operation(summary = "分页查询积分日志", description = "分页查询当前用户的积分变动记录")
    @GetMapping
    public PageResp<ApiPointsLogResp> page(@Valid PointsLogPageReq req) {
        return pointsLogService.page(req);
    }
}
