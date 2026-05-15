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

import top.continew.admin.customer.model.req.SuggestionCreateReq;
import top.continew.admin.customer.model.req.SuggestionPageReq;
import top.continew.admin.hrcommon.model.resp.ApiSuggestionResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 客户端建议服务接口
 *
 * @author weilai
 * @since 2026/05/08
 */
public interface ApiSuggestionService {

    /**
     * 提交建议
     *
     * @param req 新增建议请求
     */
    void create(SuggestionCreateReq req);

    /**
     * 客户端分页查询当前用户的建议列表（只查询标签、内容和时间）
     *
     * @param req 查询请求
     * @return 建议分页结果
     */
    PageResp<ApiSuggestionResp> apiPage(SuggestionPageReq req);
}
