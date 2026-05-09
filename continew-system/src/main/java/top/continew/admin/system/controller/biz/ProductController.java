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

package top.continew.admin.system.controller.biz;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import top.continew.admin.hrcommon.model.resp.ProductDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductResp;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.ProductQuery;
import top.continew.admin.system.model.req.ProductReq;

import top.continew.admin.system.service.ProductService;

/**
 * 商品表管理 API
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
@Tag(name = "商品表管理 API")
@RestController
@CrudRequestMapping(value = "/biz/product", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT, Api.DICT})
public class ProductController extends BaseController<ProductService, ProductResp, ProductDetailResp, ProductQuery, ProductReq> {

    @Operation(summary = "下架商品", description = "根据商品ID下架商品")
    @Parameter(name = "productId", description = "商品ID", example = "1", in = ParameterIn.PATH)
    @Parameter(name = "deleted", description = "是否下架（1：下架；0：上架）", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("biz:product:disable")
    @PutMapping("/disableProduct/{productId}/{isShelf}")
    public void disableProduct(@PathVariable Long productId, @PathVariable Boolean isShelf) {
        baseService.disableProduct(productId, isShelf);
    }
}