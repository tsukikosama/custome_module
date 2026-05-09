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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.WishQuery;
import top.continew.admin.system.model.req.WishToProductReq;
import top.continew.admin.system.model.req.WishReq;
import top.continew.admin.hrcommon.model.resp.WishDetailResp;
import top.continew.admin.hrcommon.model.resp.WishResp;
import top.continew.admin.system.service.WishService;

import jakarta.validation.Valid;

/**
 * 心愿表管理 API
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Tag(name = "心愿表管理 API")
@RestController
@CrudRequestMapping(value = "/biz/wish", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE, Api.EXPORT,
    Api.DICT})
public class WishController extends BaseController<WishService, WishResp, WishDetailResp, WishQuery, WishReq> {

    @Operation(summary = "心愿转商品", description = "将心愿转换为商品")
    @SaCheckPermission("biz:wish:toProduct")
    @PostMapping("/wishToProduct")
    public void wishToProduct(@Valid @RequestBody WishToProductReq request) {
        baseService.wishToProduct(request);
    }
}