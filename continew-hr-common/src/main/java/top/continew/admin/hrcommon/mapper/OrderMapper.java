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

package top.continew.admin.hrcommon.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 订单 Mapper
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

    /**
     * 查询本月某商品订单总数量
     *
     * @param productId 商品ID
     * @return 订单数量
     */
    Integer countByProductInCurrentMonth(@Param("productId") Long productId);

    IPage<OrderResp> customPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<OrderDO> wrapper);

    /**
     * 客户端分页查询订单
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 订单响应分页结果
     */
    IPage<OrderResp> apiCustomPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<OrderDO> wrapper);
}
