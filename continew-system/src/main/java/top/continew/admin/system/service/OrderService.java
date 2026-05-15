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

package top.continew.admin.system.service;

import jakarta.servlet.http.HttpServletResponse;
import top.continew.admin.common.base.service.BaseService;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.admin.system.model.query.OrderQuery;
import top.continew.admin.system.model.req.OrderReq;
import top.continew.starter.data.service.IService;
import top.continew.starter.extension.crud.model.query.SortQuery;

/**
 * 订单业务接口
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
public interface OrderService extends BaseService<OrderResp, OrderDetailResp, OrderQuery, OrderReq>, IService<OrderDO> {

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     */
    void cancelOrder(Long orderId);

    /**
     * 导出订单数据
     *
     * @param query     查询条件
     * @param sortQuery 排序条件
     * @param response  HTTP响应
     */
    void export(OrderQuery query, SortQuery sortQuery, HttpServletResponse response);
}