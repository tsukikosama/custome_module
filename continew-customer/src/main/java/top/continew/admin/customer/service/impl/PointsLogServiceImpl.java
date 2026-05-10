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
import top.continew.admin.customer.model.req.PointsLogPageReq;
import top.continew.admin.customer.model.resp.ApiPointsLogResp;
import top.continew.admin.customer.service.PointsLogService;
import top.continew.admin.hrcommon.mapper.PointsLogMapper;
import top.continew.admin.hrcommon.mapper.dept.DeptMapper;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.PointsLogDO;
import top.continew.admin.hrcommon.model.entity.dept.DeptDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.hrcommon.model.resp.PointsLogResp;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分日志服务实现
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Service
@RequiredArgsConstructor
public class PointsLogServiceImpl implements PointsLogService {

    private final PointsLogMapper pointsLogMapper;
    private final DeptMapper deptMapper;
    private final UserMapper userMapper;
    private static final String OPERATIONS_DEPT_ID = "699380764";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageResp<ApiPointsLogResp> page(PointsLogPageReq req) {
        // 获取当前登录用户ID和部门ID
        Long userId = StpUtil.getLoginIdAsLong();
        UserDO user = userMapper.selectById(userId);
        CheckUtils.throwIfNull(user, "用户不存在");
        Long deptId = user.getDeptId();

        // 创建分页对象
        Page<PointsLogResp> page = new Page<>(req.getPage(), req.getSize());

        // 构建查询条件
        QueryWrapper<PointsLogDO> wrapper = new QueryWrapper<>();

        // 只查询当前用户的积分日志
        wrapper.eq("user_id", userId);

        // 部门权限过滤：如果不是运营部门，则过滤掉type=1（加班转换）的记录
        if (deptId != null) {
            DeptDO dept = deptMapper.selectById(deptId);
            if (dept != null && dept.getAncestors() != null && !dept.getAncestors().contains(OPERATIONS_DEPT_ID)) {
                // 不是运营部门，过滤掉加班转换（type=1）的记录
                wrapper.ne("type", PointsTypeEnum.INCREASE);
            }
        }

        // 按时间范围筛选（支持两种方式：times数组 或 startTime+endTime）
        LocalDateTime[] timeRange = null;
        String[] times = req.getTimes();
        if (times != null && times.length == 2) {
            // 如果有times数组，解析为LocalDateTime
            timeRange = new LocalDateTime[] {
                LocalDateTime.parse(times[0], DATE_TIME_FORMATTER),
                LocalDateTime.parse(times[1], DATE_TIME_FORMATTER)
            };
        } else if (req.getStartTime() != null && req.getEndTime() != null) {
            // 如果没有times数组，尝试使用startTime和endTime
            timeRange = new LocalDateTime[] {req.getStartTime(), req.getEndTime()};
        }
        if (timeRange != null && timeRange.length == 2) {
            wrapper.ge("create_time", timeRange[0]).le("create_time", timeRange[1]);
        }

        // 按积分类型筛选
        if (req.getType() != null) {
            wrapper.eq("type", PointsTypeEnum.values()[req.getType() - 1]);
        }

        // 处理排序
        if (StringUtils.hasText(req.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(req.getSortOrder());

            if ("createTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "create_time");
            } else if ("amount".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "points");
            } else {
                wrapper.orderByDesc("create_time");
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc("create_time");
        }

        // 执行分页查询
        IPage<PointsLogResp> result = pointsLogMapper.customPage(page, wrapper);

        // 转换为客户端API响应格式
        List<ApiPointsLogResp> apiRecords = result.getRecords()
            .stream()
            .map(this::convertToApiResp)
            .collect(Collectors.toList());

        // 构建客户端API分页结果
        Page<ApiPointsLogResp> apiPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        apiPage.setRecords(apiRecords);

        return PageResp.build(apiPage);
    }

    /**
     * 转换为客户端API响应格式
     *
     * @param resp 积分日志响应
     * @return 客户端API响应
     */
    private ApiPointsLogResp convertToApiResp(PointsLogResp resp) {
        ApiPointsLogResp apiResp = new ApiPointsLogResp();
        apiResp.setId(resp.getId());
        apiResp.setTime(resp.getCreateTime().format(DATE_TIME_FORMATTER));
        apiResp.setType(resp.getType().getValue());
        apiResp.setTypeText(resp.getType().getDescription());
        apiResp.setAmount(BigDecimal.valueOf(resp.getPoints()));
        apiResp.setRemark(resp.getRemark());
        return apiResp;
    }
}
