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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.resp.ApiDeptDetailResp;
import top.continew.admin.customer.model.resp.ApiDeptResp;
import top.continew.admin.customer.service.DeptService;
import top.continew.starter.log.annotation.Log;
import top.continew.starter.web.model.R;

import java.util.List;

/**
 * 部门管理 API
 *
 * @author weilai
 * @since 2026/05/08
 */
@Tag(name = "部门管理 API")
@Log(module = "部门管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dept")
public class ApiDeptController {

    private final DeptService deptService;

    @SaIgnore
    @Operation(summary = "查询全部部门", description = "查询所有启用的部门信息，按排序字段升序排列")
    @GetMapping
    public R<List<ApiDeptResp>> list() {
        return R.ok(deptService.list());
    }

    @SaIgnore
    @Operation(summary = "查询子部门列表", description = "查询指定部门的所有直接子部门，每个子部门包含其成员列表")
    @GetMapping("/{deptId}")
    public R<List<ApiDeptDetailResp>> getDetail(@Parameter(description = "父部门ID", required = true) @PathVariable Long deptId) {
        return R.ok(deptService.getDetail(deptId));
    }
}
