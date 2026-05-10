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

import top.continew.admin.customer.model.req.ActivityImageCreateReq;
import top.continew.admin.hrcommon.model.resp.ApiActivityImageResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 活动图片服务接口
 *
 * @author weilai
 * @since 2026/05/08
 */
public interface ActivityImageService {

    /**
     * 分页查询活动图片
     *
     * @param activityId 活动ID
     * @param page       当前页码
     * @param size       每页数量
     * @return 活动图片列表
     */
    PageResp<ApiActivityImageResp> page(Long activityId, Integer page, Integer size);

    /**
     * 保存活动图片
     *
     * @param req 创建请求
     */
    void save(ActivityImageCreateReq req);

    /**
     * 删除活动图片
     *
     * @param id 图片ID
     */
    void delete(Long id);
}
