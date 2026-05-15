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

import top.continew.admin.common.base.service.BaseService;
import top.continew.admin.hrcommon.model.entity.ProductOrderLogDO;
import top.continew.admin.system.model.query.ProductOrderLogQuery;
import top.continew.admin.system.model.req.ProductOrderLogReq;
import top.continew.admin.hrcommon.model.resp.OrderLogResp;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogResp;
import top.continew.starter.data.service.IService;

import java.util.List;

/**
 * 订单状态日志业务接口
 *
 * @author weilai
 * @since 2026/01/15 15:41
 */
public interface ProductOrderLogService extends BaseService<ProductOrderLogResp, ProductOrderLogDetailResp, ProductOrderLogQuery, ProductOrderLogReq>, IService<ProductOrderLogDO> {

    /**
     * 根据订单ID查询该订单的全部日志数据
     *
     * @param orderId 订单ID
     * @return 日志列表
     */
    List<OrderLogResp> listAllByOrderId(Long orderId);
}