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
import top.continew.admin.customer.model.req.WishCreateReq;
import top.continew.admin.customer.model.req.WishPageReq;
import top.continew.admin.customer.model.resp.ApiWishResp;
import top.continew.admin.customer.service.WishService;
import top.continew.admin.hrcommon.mapper.WishMapper;
import top.continew.admin.hrcommon.model.entity.WishDO;
import top.continew.admin.hrcommon.model.enums.WishStatusEnum;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 心愿服务实现
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishMapper wishMapper;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void create(WishCreateReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        WishDO wishDO = new WishDO();

        // 如果提供了wishId，则作为父心愿ID；否则为0表示这是父心愿
        if (req.getWishId() != null) {
            wishDO.setParentsId(req.getWishId());
            wishDO.setIsProduct(false);
        } else {
            wishDO.setParentsId(0L);
        }

        wishDO.setName(req.getName());

        wishDO.setStatus(WishStatusEnum.IN_PROGRESS);

        wishDO.setCreateUser(userId);
        wishDO.setCreateTime(LocalDateTime.now());

        wishMapper.insert(wishDO);
    }

    @Override
    public PageResp<ApiWishResp> customPage(WishPageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 创建分页对象
        Page<WishDO> page = new Page<>(req.getCurrent(), req.getSize());

        // 构建查询条件
        QueryWrapper<WishDO> wrapper = new QueryWrapper<>();

        // 只查询当前用户创建的心愿（包括父心愿和子心愿）
        wrapper.eq("create_user", userId);
        wrapper.eq("deleted", 0);

        // 处理排序
        if (StringUtils.hasText(req.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(req.getSortOrder());

            if ("createTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "create_time");
            } else if ("status".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "status");
            } else {
                wrapper.orderByDesc("create_time");
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc("create_time");
        }

        // 执行分页查询（使用 MyBatis Plus 的 selectPage）
        IPage<WishDO> result = wishMapper.selectPage(page, wrapper);

        // 转换为客户端API响应格式
        List<ApiWishResp> apiRecords = result.getRecords()
            .stream()
            .map(this::convertToDoToApiResp)
            .collect(Collectors.toList());

        // 构建客户端API分页结果
        Page<ApiWishResp> apiPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        apiPage.setRecords(apiRecords);

        return PageResp.build(apiPage);
    }

    @Override
    public PageResp<ApiWishResp> allWish(WishPageReq req) {
        // 创建分页对象
        Page<WishDO> page = new Page<>(req.getCurrent(), req.getSize());

        // 构建查询条件
        QueryWrapper<WishDO> wrapper = new QueryWrapper<>();

        // 查询所有父心愿，不限制用户
        wrapper.eq("deleted", 0);
        wrapper.eq("parents_id", 0);

        // 按创建时间倒序排列（最新的心愿在前）
        wrapper.orderByDesc("create_time");

        // 执行分页查询（使用 MyBatis Plus 的 selectPage）
        IPage<WishDO> result = wishMapper.selectPage(page, wrapper);

        // 转换为客户端API响应格式
        List<ApiWishResp> apiRecords = result.getRecords()
            .stream()
            .map(this::convertToDoToApiResp)
            .collect(Collectors.toList());

        // 构建客户端API分页结果
        Page<ApiWishResp> apiPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        apiPage.setRecords(apiRecords);

        return PageResp.build(apiPage);
    }

    @Override
    public void cancelWish(Long id) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询心愿记录
        WishDO wishDO = wishMapper.selectById(id);
        if (wishDO == null) {
            throw new RuntimeException("心愿记录不存在");
        }

        // 校验是否为当前用户的心愿
        if (!wishDO.getCreateUser().equals(userId)) {
            throw new RuntimeException("无权操作此心愿记录");
        }

        // 校验心愿状态是否为"心愿中"
        if (wishDO.getStatus() != WishStatusEnum.IN_PROGRESS) {
            throw new RuntimeException("只能取消心愿中的记录");
        }

        // 使用MyBatis Plus的逻辑删除功能
        wishMapper.deleteById(id);
    }

    /**
     * 将 WishDO 转换为客户端API响应格式
     *
     * @param wishDO 心愿实体
     * @return 客户端API响应
     */
    private ApiWishResp convertToDoToApiResp(WishDO wishDO) {
        ApiWishResp apiResp = new ApiWishResp();
        apiResp.setId(wishDO.getId());
        apiResp.setTitle(wishDO.getName());
        apiResp.setCreateTime(wishDO.getCreateTime().format(DATE_TIME_FORMATTER));
        apiResp.setStatus(wishDO.getStatus().getValue());
        apiResp.setStatusText(wishDO.getStatus().getDescription());
        apiResp.setIsProduct(wishDO.getIsProduct() != null ? wishDO.getIsProduct() : false);
        apiResp.setReason(wishDO.getFailReason());
        return apiResp;
    }
}
