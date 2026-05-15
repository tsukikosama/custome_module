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

import top.continew.admin.hrcommon.model.resp.ApiCarouselResp;

import java.util.List;

/**
 * 客户端轮播图服务
 *
 * @author weilai
 * @since 2026/05/09
 */
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     *
     * @return 轮播图列表
     */
    List<ApiCarouselResp> list();

    /**
     * 根据ID查询轮播图详情
     *
     * @param id 轮播图ID
     * @return 轮播图详情
     */
    ApiCarouselResp getById(Long id);
}
