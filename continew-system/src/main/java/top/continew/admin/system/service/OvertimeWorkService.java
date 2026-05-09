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
import top.continew.admin.hrcommon.model.entity.OvertimeWorkDO;
import top.continew.admin.system.model.query.OvertimeWorkQuery;
import top.continew.admin.system.model.req.OvertimeWorkReq;
import top.continew.admin.hrcommon.model.resp.OvertimeWorkDetailResp;
import top.continew.admin.hrcommon.model.resp.OvertimeWorkResp;
import top.continew.starter.data.service.IService;

import java.util.List;

/**
 * 加班记录业务接口
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
public interface OvertimeWorkService extends BaseService<OvertimeWorkResp, OvertimeWorkDetailResp, OvertimeWorkQuery, OvertimeWorkReq>, IService<OvertimeWorkDO> {

    /**
     * 获取今日全部加班记录
     *
     * @return 今日加班记录列表
     */
    List<OvertimeWorkDO> getTodayOvertimeWorkRecord();
}