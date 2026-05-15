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

import top.continew.admin.customer.model.req.WishCreateReq;
import top.continew.admin.customer.model.req.WishPageReq;
import top.continew.admin.customer.model.resp.ApiWishResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 心愿服务接口
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
public interface WishService {

    /**
     * 新增心愿
     *
     * @param req 新增心愿请求
     */
    void create(WishCreateReq req);

    /**
     * 分页查询当前用户的心愿记录
     *
     * @param req 查询请求
     * @return 心愿分页结果
     */
    PageResp<ApiWishResp> customPage(WishPageReq req);

    /**
     * 分页查询全部心愿列表（所有用户提交的父心愿）
     *
     * @param req 查询请求
     * @return 心愿分页结果
     */
    PageResp<ApiWishResp> allWish(WishPageReq req);

    /**
     * 取消心愿
     * 校验心愿状态是否为"心愿中"，如果是则删除该心愿记录
     *
     * @param id 心愿ID
     */
    void cancelWish(Long id);
}
