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

import top.continew.admin.customer.model.req.PointsLogPageReq;
import top.continew.admin.customer.model.resp.ApiPointsLogResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 积分日志服务接口
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
public interface PointsLogService {

    /**
     * 分页查询当前用户的积分日志
     *
     * @param req 查询请求
     * @return 积分日志分页结果
     */
    PageResp<ApiPointsLogResp> page(PointsLogPageReq req);
}
