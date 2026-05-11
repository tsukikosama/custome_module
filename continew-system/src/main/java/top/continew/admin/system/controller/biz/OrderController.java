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
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.OrderQuery;
import top.continew.admin.system.model.req.OrderReq;
import top.continew.admin.system.service.OrderService;

/**
 * 订单管理 API
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Tag(name = "订单管理 API")
@RestController
@CrudRequestMapping(value = "/biz/order", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT, Api.DICT})
public class OrderController extends BaseController<OrderService, OrderResp, OrderDetailResp, OrderQuery, OrderReq> {

    @Operation(summary = "取消订单", description = "根据订单ID取消订单")
    @Parameter(name = "orderId", description = "订单ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("biz:order:refund")
    @PutMapping("/refund/{orderId}")
    public void cancelOrder(@PathVariable Long orderId) {
        // 调用Service层方法取消订单
        baseService.cancelOrder(orderId);
    }

}