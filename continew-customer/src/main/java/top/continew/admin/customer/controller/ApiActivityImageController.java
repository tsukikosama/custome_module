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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.ActivityImageCreateReq;
import top.continew.admin.customer.service.ActivityImageService;
import top.continew.admin.hrcommon.model.resp.ApiActivityImageResp;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 活动图片 API
 *
 * @author weilai
 * @since 2026/05/08
 */
@Tag(name = "活动图片 API")
@Log(module = "活动图片")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activityImage")
public class ApiActivityImageController {

    private final ActivityImageService activityImageService;

    @SaIgnore
    @Operation(summary = "分页查询活动图片", description = "根据活动ID分页查询该活动的图片列表")
    @GetMapping("/{activityId}")
    public PageResp<ApiActivityImageResp> pageImages(@Parameter(description = "活动ID", required = true) @PathVariable Long activityId,
                                                     @Parameter(description = "当前页码", required = true) @RequestParam(defaultValue = "1") Integer page,
                                                     @Parameter(description = "每页数量", required = true) @RequestParam(defaultValue = "10") Integer size) {
        return activityImageService.page(activityId, page, size);
    }

    @Operation(summary = "上传活动图片", description = "为指定活动添加图片URL")
    @PostMapping
    public void save(@RequestBody @Valid ActivityImageCreateReq req) {
        activityImageService.save(req);
    }

    @Operation(summary = "删除活动图片", description = "删除指定的活动图片，只能删除自己上传的图片")
    @Parameter(name = "id", description = "图片ID", required = true, example = "1")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityImageService.delete(id);
    }
}
