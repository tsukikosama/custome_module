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
import top.continew.admin.hrcommon.model.entity.ActivityDO;
import top.continew.admin.hrcommon.model.resp.ActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityResp;
import top.continew.admin.system.model.query.ActivityQuery;
import top.continew.admin.system.model.req.ActivityReq;
import top.continew.admin.system.model.req.ActivityReviewReq;
import top.continew.starter.data.service.IService;

/**
 * 活动业务接口
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
public interface ActivityService extends BaseService<ActivityResp, ActivityDetailResp, ActivityQuery, ActivityReq>, IService<ActivityDO> {

    /**
     * 审核活动
     *
     *
     * @param req 审核参数
     */
    void review(ActivityReviewReq req);

}