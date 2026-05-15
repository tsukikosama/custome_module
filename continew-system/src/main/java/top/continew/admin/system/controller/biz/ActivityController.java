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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.hrcommon.model.resp.ActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityResp;
import top.continew.admin.system.model.query.ActivityQuery;
import top.continew.admin.system.model.req.ActivityReq;
import top.continew.admin.system.model.req.ActivityReviewReq;
import top.continew.admin.system.service.ActivityService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * 活动管理 API
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Tag(name = "活动管理 API")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/biz/activity", api = {Api.PAGE, Api.GET, Api.CREATE, Api.BATCH_DELETE, Api.EXPORT,
    Api.UPDATE, Api.DICT})
public class ActivityController extends BaseController<ActivityService, ActivityResp, ActivityDetailResp, ActivityQuery, ActivityReq> {

    /**
     * 审核活动
     *
     * @param req 审核参数
     * @return 是否成功
     */
    @Operation(summary = "审核活动")
    @SaCheckPermission("biz:activity:review")
    @PutMapping("/approval")
    public void review(@RequestBody ActivityReviewReq req) {
        this.baseService.review(req);

    }

}