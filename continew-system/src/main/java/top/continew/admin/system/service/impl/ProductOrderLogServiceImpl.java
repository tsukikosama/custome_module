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

package top.continew.admin.system.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.hrcommon.mapper.ProductOrderLogMapper;
import top.continew.admin.hrcommon.model.entity.ProductOrderLogDO;
import top.continew.admin.system.model.query.ProductOrderLogQuery;
import top.continew.admin.system.model.req.ProductOrderLogReq;
import top.continew.admin.hrcommon.model.resp.OrderLogResp;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductOrderLogResp;
import top.continew.admin.system.service.ProductOrderLogService;

import java.util.List;

/**
 * 订单状态日志业务实现
 *
 * @author weilai
 * @since 2026/01/15 15:41
 */
@Service
@RequiredArgsConstructor
public class ProductOrderLogServiceImpl extends BaseServiceImpl<ProductOrderLogMapper, ProductOrderLogDO, ProductOrderLogResp, ProductOrderLogDetailResp, ProductOrderLogQuery, ProductOrderLogReq> implements ProductOrderLogService {
    @Override
    public Long create(ProductOrderLogReq req) {

        // 获取当前用户名
        String username = UserContextHolder.getUsername();
        if (req.getAfterStatus() == null) {
            req.setRemark(String.format("用户：%s 的订单 %s 已下单", username, req.getOrderId()));
        } else {
            // 设置备注信息
            req.setRemark(String.format("用户%s 的订单 %s状态从 %s 更新到 %s", username, req.getOrderId(), req.getAfterStatus()
                .getDescription(), req.getStatus().getDescription()));
        }

        return super.create(req);
    }

    @Override
    public List<OrderLogResp> listAllByOrderId(Long orderId) {
        // 调用Mapper中的方法查询
        List<OrderLogResp> orderLogList = baseMapper.selectListByOrderId(orderId);

        // 转换为响应对象
        return orderLogList;
    }

}