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

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.hrcommon.model.resp.ActivityMemberCustomResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberResp;
import top.continew.admin.system.model.req.ActivityMemberReq;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.extension.crud.model.query.PageQuery;

import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.ActivityMemberQuery;
import top.continew.admin.system.service.ActivityMemberService;

/**
 * 活动参与用户管理 API
 *
 * @author weilai
 * @since 2026/05/06 11:03
 */
@Tag(name = "活动参与用户管理 API")
@RestController
@CrudRequestMapping(value = "/biz/activityMember", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT, Api.DICT})
public class ActivityMemberController extends BaseController<ActivityMemberService, ActivityMemberResp, ActivityMemberDetailResp, ActivityMemberQuery, ActivityMemberReq> {

    @Operation(summary = "自定义分页查询活动参与用户", description = "根据活动ID查询演讲人、必参加人、主动参加人")
    @PostMapping("/customPage")
    @SaCheckPermission("biz:activityMember:page")
    public PageResp<ActivityMemberCustomResp> customPage(@RequestBody ActivityMemberQuery query,
                                                         @ModelAttribute PageQuery pageQuery) {
        return PageResp.build(baseService.customPage(query, pageQuery));
    }
}
