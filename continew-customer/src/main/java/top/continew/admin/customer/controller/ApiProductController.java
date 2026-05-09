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
import top.continew.admin.customer.model.query.ApiProductQuery;
import top.continew.admin.customer.model.resp.ApiProductDetailResp;
import top.continew.admin.customer.model.resp.ApiProductResp;
import top.continew.admin.customer.service.ProductService;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 商品 API
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Tag(name = "商品 API")
@Log(module = "商品管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ApiProductController {

    private final ProductService apiProductService;

    @Operation(summary = "分页查询商品", description = "分页查询已上架的商品列表")
    @GetMapping
    @SaIgnore
    public PageResp<ApiProductResp> page(@Valid ApiProductQuery req) {
        return apiProductService.page(req);
    }

    @Operation(summary = "查询商品详情", description = "查询单个商品的详细信息")
    @Parameter(name = "id", description = "商品ID", required = true, example = "1")
    @GetMapping("/{id}")
    @SaIgnore
    public ApiProductDetailResp getDetail(@PathVariable Long id) {
        return apiProductService.getDetail(id);
    }
}
