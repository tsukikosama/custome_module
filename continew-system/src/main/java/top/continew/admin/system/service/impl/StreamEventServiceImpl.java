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

import top.continew.admin.system.model.req.StreamEventReq;
import top.continew.admin.system.service.StreamEventService;
import top.continew.starter.data.service.impl.ServiceImpl;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.BasePageResp;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.admin.hrcommon.mapper.StreamEventMapper;
import top.continew.admin.hrcommon.model.entity.StreamEventDO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件日志表业务实现
 *
 * @author weilai
 * @since 2026/01/19 11:42
 */
@Service
@RequiredArgsConstructor
public class StreamEventServiceImpl extends ServiceImpl<StreamEventMapper, StreamEventDO> implements StreamEventService {
    @Override
    public List<StreamEventDO> getTodayAllRecord() {
        // 获取当前日期的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 查询今日所有记录  因为加班判断的bizCategoryId 为空字符串 只能判断是否有加班的
        return this.baseMapper.selectList(Wrappers.<StreamEventDO>lambdaQuery()
            .like(StreamEventDO::getContent, "加班")
            .ge(StreamEventDO::getCreateTime, todayStart)
            .le(StreamEventDO::getCreateTime, todayEnd));
    }

    @Override
    public BasePageResp<StreamEventDO> customPage(StreamEventReq query, PageQuery pageQuery) {
        QueryWrapper<StreamEventDO> wrapper = new QueryWrapper<>();
        // 执行分页查询
        IPage<StreamEventDO> page = this.baseMapper.selectOvertimeWorkPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);

        // 构建并返回分页结果
        return PageResp.build(page);
    }
}
