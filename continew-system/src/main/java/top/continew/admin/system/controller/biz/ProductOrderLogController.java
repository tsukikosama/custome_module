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
import top.continew.admin.hrcommon.model.resp.OrderLogResp;
import top.continew.starter.extension.crud.enums.Api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.ProductOrderLogQuery;
import top.continew.admin.system.model.req.ProductOrderLogReq;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogResp;
import top.continew.admin.system.service.ProductOrderLogService;

import java.util.List;

/**
 * 订单状态日志管理 API
 *
 * @author weilai
 * @since 2026/01/15 15:41
 */
@Tag(name = "订单状态日志管理 API")
@RestController
@CrudRequestMapping(value = "/biz/productOrderLog", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT, Api.DICT})
public class ProductOrderLogController extends BaseController<ProductOrderLogService, ProductOrderLogResp, ProductOrderLogDetailResp, ProductOrderLogQuery, ProductOrderLogReq> {

    @Operation(summary = "查询订单全部日志", description = "根据订单ID查询该订单的全部日志数据")
    @Parameter(name = "orderId", description = "订单ID", example = "1", in = ParameterIn.PATH)
    @GetMapping("/list/{orderId}")
    @SaCheckPermission("biz:productOrderLog:list")
    public List<OrderLogResp> listAllByOrderId(@PathVariable Long orderId) {
        return baseService.listAllByOrderId(orderId);
    }
}