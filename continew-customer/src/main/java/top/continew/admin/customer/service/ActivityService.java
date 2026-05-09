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

import top.continew.admin.customer.model.req.ActivityCreateReq;
import top.continew.admin.customer.model.req.ActivityPageReq;
import top.continew.admin.hrcommon.model.resp.ApiActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ApiActivityResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 客户端活动服务接口
 *
 * @author weilai
 * @since 2026/05/08
 */
public interface ActivityService {

    /**
     * 分页查询已审核通过的活动
     *
     * @param req 分页查询请求
     * @return 活动列表
     */
    PageResp<ApiActivityResp> page(ActivityPageReq req);

    /**
     * 查询活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    ApiActivityDetailResp getActivityDetail(Long id);

    /**
     * 创建活动申请
     *
     * @param req 创建活动请求
     */
    void create(ActivityCreateReq req);

    /**
     * 报名活动
     *
     * @param activityId 活动ID
     */
    void participate(Long activityId);

    /**
     * 取消报名
     *
     * @param activityId 活动ID
     */
    void cancelParticipate(Long activityId);
}
