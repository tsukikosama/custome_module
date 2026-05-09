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
import top.continew.admin.hrcommon.model.entity.PointsLogDO;
import top.continew.admin.system.model.query.PointsLogQuery;
import top.continew.admin.system.model.req.PointsLogReq;
import top.continew.admin.hrcommon.model.resp.PointsLogDetailResp;
import top.continew.admin.hrcommon.model.resp.PointsLogResp;
import top.continew.starter.data.service.IService;

/**
 * 积分日志业务接口
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
public interface PointsLogService extends BaseService<PointsLogResp, PointsLogDetailResp, PointsLogQuery, PointsLogReq>, IService<PointsLogDO> {}