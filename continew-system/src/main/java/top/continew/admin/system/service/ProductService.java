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
import top.continew.admin.hrcommon.model.entity.ProductDO;
import top.continew.admin.system.model.query.ProductQuery;
import top.continew.admin.system.model.req.ProductReq;
import top.continew.admin.hrcommon.model.resp.ProductDetailResp;
import top.continew.admin.hrcommon.model.resp.ProductResp;
import top.continew.starter.data.service.IService;

/**
 * 商品表业务接口
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
public interface ProductService extends BaseService<ProductResp, ProductDetailResp, ProductQuery, ProductReq>, IService<ProductDO> {

    /**
     * 下架商品
     *
     * @param productId 商品ID
     * @param isShelf   是否下架（1：下架；0：上架）
     */
    void disableProduct(Long productId, Boolean isShelf);

    void checkIsSameProductName(String productName);
}