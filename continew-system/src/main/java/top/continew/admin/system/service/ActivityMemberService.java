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

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.continew.admin.common.base.service.BaseService;
import top.continew.admin.controller.biz.model.entity.ActivityMemberDO;
import top.continew.admin.hrcommon.model.resp.ActivityMemberCustomResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberResp;
import top.continew.admin.system.model.query.ActivityMemberQuery;
import top.continew.admin.system.model.req.ActivityMemberReq;
import top.continew.starter.data.service.IService;
import top.continew.starter.extension.crud.model.query.PageQuery;

/**
 * 活动参与用户业务接口
 *
 * @author weilai
 * @since 2026/05/06 11:03
 */
public interface ActivityMemberService extends BaseService<ActivityMemberResp, ActivityMemberDetailResp, ActivityMemberQuery, ActivityMemberReq>, IService<ActivityMemberDO> {

    /**
     * 自定义分页查询活动参与用户
     *
     * @param query     查询条件
     * @param pageQuery 分页条件
     * @return 活动参与用户自定义分页响应
     */
    IPage<ActivityMemberCustomResp> customPage(ActivityMemberQuery query, PageQuery pageQuery);
}
