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

package top.continew.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.controller.biz.model.entity.ActivityMemberDO;
import top.continew.admin.hrcommon.mapper.ActivityMemberMapper;
import top.continew.admin.hrcommon.model.resp.ActivityMemberCustomResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityMemberResp;
import top.continew.admin.system.model.query.ActivityMemberQuery;
import top.continew.admin.system.model.req.ActivityMemberReq;
import top.continew.admin.system.service.ActivityMemberService;
import top.continew.starter.extension.crud.model.query.PageQuery;

/**
 * 活动参与用户业务实现
 *
 * @author weilai
 * @since 2026/05/06 11:03
 */
@Service
@RequiredArgsConstructor
public class ActivityMemberServiceImpl extends BaseServiceImpl<ActivityMemberMapper, ActivityMemberDO, ActivityMemberResp, ActivityMemberDetailResp, ActivityMemberQuery, ActivityMemberReq> implements ActivityMemberService {

    @Override
    public IPage<ActivityMemberCustomResp> customPage(ActivityMemberQuery query, PageQuery pageQuery) {
        QueryWrapper<ActivityMemberDO> wrapper = new QueryWrapper<>();
        // 可以在这里添加额外的查询条件
        IPage<ActivityMemberCustomResp> page = this.baseMapper.customPage(new Page(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);
        return page;
    }
}
