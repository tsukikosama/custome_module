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

package top.continew.admin.system.controller.biz;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.hrcommon.model.resp.SuggestionDetailResp;
import top.continew.admin.hrcommon.model.resp.SuggestionResp;
import top.continew.admin.system.model.query.SuggestionQuery;
import top.continew.admin.system.model.req.SuggestionReq;
import top.continew.admin.system.service.SuggestionService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * 建议管理 API
 *
 * @author weilai
 * @since 2026/05/06 18:15
 */
@Tag(name = "建议管理 API")
@RestController
@CrudRequestMapping(value = "/biz/suggestion", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT, Api.DICT})
public class SuggestionController extends BaseController<SuggestionService, SuggestionResp, SuggestionDetailResp, SuggestionQuery, SuggestionReq> {}