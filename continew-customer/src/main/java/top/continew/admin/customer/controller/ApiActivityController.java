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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.ActivityCreateReq;
import top.continew.admin.customer.model.req.ActivityPageReq;
import top.continew.admin.hrcommon.model.resp.ApiActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ApiActivityResp;
import top.continew.admin.customer.service.ActivityService;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;
import top.continew.starter.web.model.R;

/**
 * 活动管理 API
 *
 * @author weilai
 * @since 2026/05/08
 */
@Tag(name = "活动管理 API")
@Log(module = "活动管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class ApiActivityController {

    private final ActivityService activityService;

    @Operation(summary = "分页查询活动", description = "分页查询已审核通过的活动")
    @GetMapping
    public PageResp<ApiActivityResp> page(@Valid ActivityPageReq req) {
        return activityService.page(req);
    }

    @Operation(summary = "查询活动详情", description = "查询单个活动的详细信息")
    @GetMapping("/{id}")
    public R<ApiActivityDetailResp> getDetail(@Parameter(description = "活动ID", required = true) @PathVariable Long id) {
        return R.ok(activityService.getActivityDetail(id));
    }

    @Operation(summary = "创建活动", description = "用户创建活动申请")
    @PostMapping
    public R<Void> create(@Valid @RequestBody ActivityCreateReq req) {
        activityService.create(req);
        return R.ok();
    }

    @Operation(summary = "报名活动", description = "用户报名参加活动")
    @PostMapping("/participate/{activityId}")
    public R<Void> participate(@Parameter(description = "活动ID", required = true) @PathVariable Long activityId) {
        activityService.participate(activityId);
        return R.ok();
    }

    @Operation(summary = "取消报名", description = "用户取消已报名的活动")
    @PutMapping("/cancel/{activityId}")
    public R<Void> cancelParticipate(@Parameter(description = "活动ID", required = true) @PathVariable Long activityId) {
        activityService.cancelParticipate(activityId);
        return R.ok();
    }

    @Operation(summary = "获取置顶活动", description = "获取全部已审核通过的置顶活动")
    @GetMapping("/pinned")
    public R<java.util.List<ApiActivityResp>> listPinnedActivities() {
        return R.ok(activityService.listPinnedActivities());
    }
}
