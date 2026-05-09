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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.hrcommon.mapper.OvertimeWorkMapper;
import top.continew.admin.hrcommon.model.entity.OvertimeWorkDO;
import top.continew.admin.system.model.query.OvertimeWorkQuery;
import top.continew.admin.system.model.req.OvertimeWorkReq;
import top.continew.admin.hrcommon.model.resp.OvertimeWorkDetailResp;
import top.continew.admin.hrcommon.model.resp.OvertimeWorkResp;
import top.continew.admin.system.service.OvertimeWorkService;
import top.continew.admin.system.service.UserService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 加班记录业务实现
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Service
@RequiredArgsConstructor
public class OvertimeWorkServiceImpl extends BaseServiceImpl<OvertimeWorkMapper, OvertimeWorkDO, OvertimeWorkResp, OvertimeWorkDetailResp, OvertimeWorkQuery, OvertimeWorkReq> implements OvertimeWorkService {

    private final UserService userService;

    @Override
    public List<OvertimeWorkDO> getTodayOvertimeWorkRecord() {
        // 获取当前日期的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 查询今日所有加班记录
        return this.baseMapper.selectList(Wrappers.<OvertimeWorkDO>lambdaQuery()
            .ge(OvertimeWorkDO::getCreateTime, todayStart)
            .le(OvertimeWorkDO::getCreateTime, todayEnd));
    }

    @Override
    public PageResp<OvertimeWorkResp> page(OvertimeWorkQuery query, PageQuery pageQuery) {
        QueryWrapper<OvertimeWorkDO> wrapper = this.buildQueryWrapper(query);
        wrapper.eq("bow.deleted", 0);
        IPage<OvertimeWorkResp> page = this.baseMapper.customPage(new Page((long)pageQuery.getPage(), (long)pageQuery
            .getSize()), wrapper);
        return PageResp.build(page);
    }
}