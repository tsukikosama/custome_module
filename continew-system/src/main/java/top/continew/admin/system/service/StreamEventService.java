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

import jakarta.validation.Valid;
import top.continew.admin.hrcommon.model.entity.StreamEventDO;
import top.continew.admin.system.model.req.StreamEventReq;
import top.continew.starter.data.service.IService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.BasePageResp;

import java.util.List;

/**
 * 事件日志表业务接口
 *
 * @author weilai
 * @since 2026/01/19 11:42
 */
public interface StreamEventService extends IService<StreamEventDO> {

    List<StreamEventDO> getTodayAllRecord();

    BasePageResp<StreamEventDO> customPage(@Valid StreamEventReq query, @Valid PageQuery pageQuery);
}
