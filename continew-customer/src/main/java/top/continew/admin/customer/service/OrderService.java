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

package top.continew.admin.customer.service;

import top.continew.admin.customer.model.req.OrderCreateReq;
import top.continew.admin.customer.model.req.OrderPageReq;
import top.continew.admin.customer.model.resp.OrderCreateResp;
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 订单服务接口
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
public interface OrderService {

    /**
     * 下单
     *
     * @param req 下单请求
     * @return 下单响应
     */
    OrderCreateResp create(OrderCreateReq req);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     */
    void cancel(Long orderId);

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailResp getDetail(Long orderId);

    /**
     * 分页查询订单
     *
     * @param req 查询请求
     * @return 订单分页结果
     */
    PageResp<OrderResp> page(OrderPageReq req);

    /**
     * 确认订单完成
     *
     * @param orderId 订单ID
     */
    void confirmCompletion(Long orderId);
}
