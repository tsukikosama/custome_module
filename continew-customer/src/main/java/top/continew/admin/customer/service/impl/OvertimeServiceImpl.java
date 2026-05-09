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

package top.continew.admin.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.continew.admin.customer.model.req.OvertimePageReq;
import top.continew.admin.customer.model.resp.ApiOvertimeResp;
import top.continew.admin.customer.service.OvertimeService;
import top.continew.admin.hrcommon.mapper.OvertimeWorkMapper;
import top.continew.admin.hrcommon.model.entity.OvertimeWorkDO;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 加班记录服务实现
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Service
@RequiredArgsConstructor
public class OvertimeServiceImpl implements OvertimeService {

    private final OvertimeWorkMapper overtimeWorkMapper;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public PageResp<ApiOvertimeResp> page(OvertimePageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 创建分页对象
        Page<OvertimeWorkDO> page = new Page<>(req.getPage(), req.getSize());

        // 构建查询条件
        QueryWrapper<OvertimeWorkDO> wrapper = new QueryWrapper<>();

        // 只查询当前用户的加班记录
        wrapper.eq("user_id", userId);

        // 按状态筛选
        if (req.getStatus() != null) {
            wrapper.eq("status", req.getStatus());
        }

        // 按结果筛选
        if (req.getResult() != null) {
            wrapper.eq("result", req.getResult());
        }

        // 按时间范围筛选（支持两种方式：times数组 或 startTime+endTime）
        LocalDateTime[] timeRange = req.getTimes();
        if (timeRange == null || timeRange.length != 2) {
            // 如果没有times数组，尝试使用startTime和endTime
            if (req.getStartTime() != null && req.getEndTime() != null) {
                timeRange = new LocalDateTime[] {req.getStartTime(), req.getEndTime()};
            }
        }
        if (timeRange != null && timeRange.length == 2) {
            wrapper.ge("create_time", timeRange[0]).le("create_time", timeRange[1]);
        }

        // 处理排序
        if (StringUtils.hasText(req.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(req.getSortOrder());

            if ("createTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "create_time");
            } else if ("points".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "convert_points");
            } else if ("overtimeDate".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "start_time");
            } else {
                wrapper.orderByDesc("create_time");
            }
        } else {
            // 默认按加班日期倒序
            wrapper.orderByDesc("start_time");
        }

        // 执行分页查询
        IPage<OvertimeWorkDO> result = overtimeWorkMapper.selectPage(page, wrapper);

        // 转换为客户端API响应格式
        List<ApiOvertimeResp> apiRecords = result.getRecords()
            .stream()
            .map(this::convertToApiResp)
            .collect(Collectors.toList());

        // 构建客户端API分页结果
        Page<ApiOvertimeResp> apiPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        apiPage.setRecords(apiRecords);

        return PageResp.build(apiPage);
    }

    /**
     * 转换为客户端API响应格式
     *
     * @param overtime 加班记录实体
     * @return 客户端API响应
     */
    private ApiOvertimeResp convertToApiResp(OvertimeWorkDO overtime) {
        ApiOvertimeResp apiResp = new ApiOvertimeResp();
        apiResp.setId(overtime.getId());

        // 使用开始时间作为加班日期
        if (overtime.getStartTime() != null) {
            apiResp.setOvertimeDate(overtime.getStartTime());
            apiResp.setStartTime(overtime.getStartTime().format(TIME_FORMATTER));
        }

        if (overtime.getEndTime() != null) {
            apiResp.setEndTime(overtime.getEndTime().format(TIME_FORMATTER));
        }

        apiResp.setDuration(overtime.getDuration());
        apiResp.setPoints(overtime.getConvertPoints());

        // 状态转换
        try {
            apiResp.setStatus(Integer.parseInt(overtime.getStatus()));
            switch (overtime.getStatus()) {
                case "1":
                    apiResp.setStatusText("待审核");
                    break;
                case "2":
                    apiResp.setStatusText("已通过");
                    break;
                case "3":
                    apiResp.setStatusText("已拒绝");
                    break;
                default:
                    apiResp.setStatusText("未知");
            }
        } catch (NumberFormatException e) {
            apiResp.setStatus(-1);
            apiResp.setStatusText("未知");
        }

        // 结果转换
        if (overtime.getResult() != null) {
            try {
                apiResp.setResult(Integer.parseInt(overtime.getResult()));
                switch (overtime.getResult()) {
                    case "1":
                        apiResp.setResultText("有效");
                        break;
                    case "2":
                        apiResp.setResultText("无效");
                        break;
                    default:
                        apiResp.setResultText("未知");
                }
            } catch (NumberFormatException e) {
                apiResp.setResult(-1);
                apiResp.setResultText("未知");
            }
        }

        if (overtime.getCreateTime() != null) {
            apiResp.setCreateTime(overtime.getCreateTime().format(DATE_TIME_FORMATTER));
        }

        return apiResp;
    }
}
