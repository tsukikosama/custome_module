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
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.hrcommon.model.entity.ProductDO;
import top.continew.admin.system.model.query.ProductQuery;
import top.continew.admin.system.model.req.ProductReq;
import top.continew.admin.hrcommon.model.resp.ProductDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductResp;
import top.continew.admin.system.service.ProductService;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.admin.hrcommon.mapper.ProductMapper;

/**
 * 商品表业务实现
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, ProductDO, ProductResp, ProductDetailResp, ProductQuery, ProductReq> implements ProductService {

    @Override
    public void disableProduct(Long productId, Boolean isShelf) {
        // 校验商品是否存在
        ProductDO product = getById(productId);
        CheckUtils.throwIfNull(product, "商品不存在");

        this.update(Wrappers.<ProductDO>lambdaUpdate()
            .eq(ProductDO::getId, productId)
            .set(ProductDO::getIsShelf, isShelf));
    }

    @Override
    protected void beforeCreate(ProductReq req) {
        // 校验商品名称是否已存在（忽略大小写和前后空格）
        checkIsSameProductName(req.getName());
        super.beforeCreate(req);
    }

    @Override
    public void checkIsSameProductName(String productName) {
        Long count = this.baseMapper.selectCount(Wrappers.<ProductDO>lambdaQuery()
            .apply("LOWER(TRIM(name)) = LOWER({0})", productName));
        CheckUtils.throwIf(count > 0, "商品名称已存在");
    }

    @Override
    public PageResp<ProductResp> page(ProductQuery query, PageQuery pageQuery) {
        QueryWrapper<ProductDO> wrapper = this.buildQueryWrapper(query);
        wrapper.lambda().orderByDesc(ProductDO::getCreateTime);
        wrapper.eq("bp.deleted", false);
        IPage<ProductResp> page = this.baseMapper.customPage(new Page((long)pageQuery.getPage(), (long)pageQuery
            .getSize()), wrapper);
        return PageResp.build(page);
    }
}