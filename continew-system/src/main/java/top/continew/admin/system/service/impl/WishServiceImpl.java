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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.hrcommon.model.entity.WishDO;
import top.continew.admin.hrcommon.model.resp.WishResp;
import top.continew.admin.system.model.query.WishQuery;
import top.continew.admin.system.model.req.ProductReq;
import top.continew.admin.system.model.req.WishToProductReq;
import top.continew.admin.system.model.req.WishReq;
import top.continew.admin.hrcommon.model.resp.WishDetailResp;
import top.continew.admin.system.service.ProductService;
import top.continew.admin.system.service.WishService;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.admin.hrcommon.mapper.WishMapper;
import top.continew.starter.excel.util.ExcelUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.query.SortQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.util.List;

/**
 * 心愿表业务实现
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Service
@RequiredArgsConstructor
public class WishServiceImpl extends BaseServiceImpl<WishMapper, WishDO, WishResp, WishDetailResp, WishQuery, WishReq> implements WishService {

    private final ProductService productService;

    @Override
    public PageResp<WishResp> page(WishQuery query, PageQuery pageQuery) {
        // 构建查询条件
        QueryWrapper<WishDO> wrapper = this.buildQueryWrapper(query);
        wrapper.eq("t1.parents_id", 0);
        wrapper.eq("t1.deleted", 0);
        wrapper.orderByDesc("t1.create_time");
        // 使用自定义分页查询（只查询父心愿，并统计子心愿数量）
        IPage<WishResp> page = baseMapper.customParentPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);

        return PageResp.build(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wishToProduct(WishToProductReq request) {
        // 参数校验
        CheckUtils.throwIfNull(request, "请求参数不能为空");
        CheckUtils.throwIfNull(request.getName(), "商品名称不能为空");
        CheckUtils.throwIfNull(request.getPoints(), "商品积分不能为空");
        CheckUtils.throwIfNull(request.getTypeId(), "商品类型ID不能为空");

        productService.checkIsSameProductName(request.getName());
        // 创建商品
        ProductReq productReq = new ProductReq();
        productReq.setName(request.getName());
        productReq.setPoints(request.getPoints());
        productReq.setTypeId(request.getTypeId());
        productReq.setImage(request.getImage());
        productReq.setDescription(request.getDescription());
        productReq.setIsShelf(true); // 默认上架
        productReq.setMonthLimit(request.getMonthLimit());
        // 保存商品
        Long productId = productService.create(productReq);

        // 更新心愿状态
        WishDO wish = new WishDO();
        wish.setProductId(productId);
        wish.setIsProduct(true);
        wish.setId(request.getId());
        baseMapper.updateById(wish);
    }

    @Override
    public Long create(WishReq req) {
        // 清空 ID，避免主键冲突
        req.setId(null);

        // 查询数据库中是否存在同名的心愿（父心愿：parentsId = 0）
        WishDO existingWish = baseMapper.selectOne(Wrappers.<WishDO>lambdaQuery()
            .eq(WishDO::getName, req.getName())
            .eq(WishDO::getParentsId, 0));

        if (existingWish != null) {
            // 如果存在同名心愿，创建子心愿，parentsId 设置为同名心愿的 id
            req.setParentsId(existingWish.getId());
        } else {
            // 如果不存在同名心愿，创建父心愿，parentsId 设置为 0
            req.setParentsId(0L);
        }

        // 创建心愿记录
        return super.create(req);
    }

    @Override
    public void export(WishQuery query, SortQuery sortQuery, HttpServletResponse response) {
        QueryWrapper<WishDO> wrapper = this.buildQueryWrapper(query);
        List<WishDetailResp> list = this.baseMapper.customList(wrapper);
        list.forEach(this::fill);
        ExcelUtils.export(list, "导出数据", WishDetailResp.class, response);
    }
}