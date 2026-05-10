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
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.continew.admin.common.base.service.BaseServiceImpl;

import top.continew.admin.system.model.query.PointsLogQuery;
import top.continew.admin.system.model.req.PointsLogReq;
import top.continew.admin.system.model.req.user.UserPointChangeReq;
import top.continew.admin.hrcommon.model.resp.PointsLogDetailResp;
import top.continew.admin.hrcommon.model.resp.PointsLogResp;
import top.continew.admin.system.service.PointsLogService;
import top.continew.admin.system.service.UserService;
import top.continew.starter.excel.util.ExcelUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.query.SortQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.admin.hrcommon.model.entity.PointsLogDO;
import top.continew.admin.hrcommon.mapper.PointsLogMapper;

import java.util.List;

/**
 * 积分日志业务实现
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Service
@RequiredArgsConstructor
public class PointsLogServiceImpl extends BaseServiceImpl<PointsLogMapper, PointsLogDO, PointsLogResp, PointsLogDetailResp, PointsLogQuery, PointsLogReq> implements PointsLogService {
    @Lazy
    @Resource
    private UserService userService;

    @Override
    public Long create(PointsLogReq req) {
        for (Long id : req.getUserIds()) {
            UserPointChangeReq changeReq = new UserPointChangeReq();
            //调用积分扣除的方法
            changeReq.setPoints(req.getPoints());
            changeReq.setType(req.getType());
            changeReq.setUserId(id);
            changeReq.setRemark(req.getRemark());
            userService.changePoints(changeReq);
        }
        return 1L;
    }

    @Override
    public PageResp<PointsLogResp> page(PointsLogQuery query, PageQuery pageQuery) {
        QueryWrapper<PointsLogDO> wrapper = this.buildQueryWrapper(query);
        wrapper.lambda().orderByDesc(PointsLogDO::getCreateTime);
        IPage<PointsLogResp> page = this.baseMapper.customPage(new Page((long)pageQuery.getPage(), (long)pageQuery
            .getSize()), wrapper);
        return PageResp.build(page);
    }

    @Override
    public void export(PointsLogQuery query, SortQuery sortQuery, HttpServletResponse response) {
        QueryWrapper<PointsLogDO> wrapper = this.buildQueryWrapper(query);
        List<PointsLogResp> list = this.baseMapper.customList(wrapper);
        ExcelUtils.export(list, "积分日志数据", PointsLogResp.class, response);
    }

}