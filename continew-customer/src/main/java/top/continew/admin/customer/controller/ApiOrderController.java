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
import top.continew.admin.customer.model.req.OrderCreateReq;
import top.continew.admin.customer.model.req.OrderPageReq;
import top.continew.admin.customer.model.resp.OrderCreateResp;
import top.continew.admin.customer.service.OrderService;
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 订单 API
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Tag(name = "订单 API")
@Log(module = "订单管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class ApiOrderController {

    private final OrderService orderService;

    @Operation(summary = "下单", description = "用户使用积分兑换商品")
    @PostMapping
    public OrderCreateResp create(@RequestBody @Valid OrderCreateReq req) {
        return orderService.create(req);
    }

    @Operation(summary = "取消订单", description = "用户取消订单，退还积分")
    @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")
    @PutMapping("/cancel/{orderId}")
    public void cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
    }

    @Operation(summary = "查询订单详情", description = "查询单个订单的详细信息")
    @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")
    @GetMapping("/{orderId}")
    public OrderDetailResp getDetail(@PathVariable Long orderId) {
        return orderService.getDetail(orderId);
    }

    @Operation(summary = "分页查询订单", description = "分页查询当前用户的订单列表")
    @GetMapping
    public PageResp<OrderResp> page(@Valid OrderPageReq req) {
        return orderService.page(req);
    }

    @Operation(summary = "确认订单完成", description = "用户确认收到商品，将订单状态从已交付更新为已完成")
    @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")
    @PutMapping("/complete/{orderId}")
    public void confirmCompletion(@PathVariable Long orderId) {
        orderService.confirmCompletion(orderId);
    }
}
