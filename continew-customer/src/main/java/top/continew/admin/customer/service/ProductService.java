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

import top.continew.admin.customer.model.query.ApiProductQuery;
import top.continew.admin.customer.model.resp.ApiProductDetailResp;
import top.continew.admin.customer.model.resp.ApiProductResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 商品API服务接口
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
public interface ProductService {

    /**
     * 分页查询商品
     *
     * @param req 查询请求
     * @return 商品分页结果
     */
    PageResp<ApiProductResp> page(ApiProductQuery req);

    /**
     * 查询商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    ApiProductDetailResp getDetail(Long id);
}
