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
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.customer.model.req.OrderCreateReq;
import top.continew.admin.customer.model.req.OrderPageReq;
import top.continew.admin.customer.model.resp.OrderCreateResp;
import top.continew.admin.customer.service.OrderService;
import top.continew.admin.hrcommon.mapper.OrderMapper;
import top.continew.admin.hrcommon.mapper.PointsLogMapper;
import top.continew.admin.hrcommon.mapper.ProductMapper;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.entity.PointsLogDO;
import top.continew.admin.hrcommon.model.entity.ProductDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单服务实现
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Service("customerOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final PointsLogMapper pointsLogMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateResp create(OrderCreateReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 校验商品是否存在且已上架
        ProductDO product = productMapper.selectById(req.getProductId());
        CheckUtils.throwIf(null == product || !product.getIsShelf(), "商品不存在或已下架");

        // 2. 查询用户积分
        UserDO user = userMapper.selectById(userId);
        CheckUtils.throwIf(null == user, "用户不存在");
        Integer userPoints = user.getPoints();

        // 3. 计算消耗积分
        BigDecimal costPoints = BigDecimal.valueOf(product.getPoints())
            .multiply(BigDecimal.valueOf(req.getProductNum()));

        // 4. 校验用户积分是否足够
        CheckUtils.throwIf(BigDecimal.valueOf(userPoints).compareTo(costPoints) < 0, String
            .format("积分不足，当前积分：%d，需要：%.2f", userPoints, costPoints));

        // 5. 校验商品月限数量
        Integer monthlyLimit = product.getMonthLimit();
        if (monthlyLimit != null && monthlyLimit > 0) {
            // 统计用户本月该商品的订单数量（排除已取消的订单）
            QueryWrapper<OrderDO> monthlyWrapper = new QueryWrapper<>();
            monthlyWrapper.eq("create_user", userId)
                .eq("product_id", req.getProductId())
                .ne("status", OrderStatusEnum.CANCELLED)
                .apply("DATE_FORMAT(create_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')");
            Long monthlyCount = orderMapper.selectCount(monthlyWrapper);

            // 校验是否超过月限
            CheckUtils.throwIf(monthlyCount + req.getProductNum() > monthlyLimit, String
                .format("超出月限数量，本月已兑换：%d，月限：%d", monthlyCount, monthlyLimit));
        }

        // 6. 生成订单号（使用雪花算法）
        String orderNo = "DD" + IdUtil.getSnowflakeNextId();

        // 7. 创建订单
        OrderDO order = new OrderDO();
        order.setOrderNo(orderNo);
        order.setProductId(req.getProductId());
        order.setProductNum(req.getProductNum());
        order.setStatus(OrderStatusEnum.PENDING);
        order.setCostPoints(costPoints);
        order.setCreateUser(userId);
        orderMapper.insert(order);

        // 8. 扣除用户积分并记录积分日志
        Integer afterPoints = userPoints - costPoints.intValue();

        // 更新用户积分
        user.setPoints(afterPoints);
        userMapper.updateById(user);

        // 记录积分日志
        PointsLogDO pointsLog = new PointsLogDO();
        pointsLog.setUserId(userId);
        pointsLog.setType(PointsTypeEnum.DECREASE);
        pointsLog.setRefId(order.getId());
        pointsLog.setPoints(-costPoints.intValue());
        pointsLog.setBeforePoints(userPoints);
        pointsLog.setAfterPoints(afterPoints);
        pointsLog.setRemark("兑换商品：" + product.getName());
        pointsLog.setStatus(PointsStatusEnum.VALID);
        pointsLogMapper.insert(pointsLog);

        return OrderCreateResp.builder().orderId(order.getId()).orderNo(orderNo).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long orderId) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        UserDO userDO = userMapper.selectById(userId);
        // 1. 查询订单
        OrderDO order = orderMapper.selectById(orderId);
        CheckUtils.throwIf(null == order, "订单不存在");

        // 2. 校验订单状态
        CheckUtils.throwIf(order.getStatus() != OrderStatusEnum.PENDING, "该订单状态不允许取消");

        // 3. 校验是否为当前用户的订单
        CheckUtils.throwIf(!order.getCreateUser().equals(userId), "无权操作此订单");

        // 4. 更新订单状态
        order.setStatus(OrderStatusEnum.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setUpdateUser(userId);
        orderMapper.updateById(order);

        // 5. 退还积分
        Integer refundPoints = order.getCostPoints().intValue();
        Integer beforePoints = userDO.getPoints();
        Integer afterPoints = beforePoints + refundPoints;

        // 更新用户积分
        userDO.setPoints(afterPoints);
        userMapper.updateById(userDO);

        // 记录积分日志
        PointsLogDO pointsLog = new PointsLogDO();
        pointsLog.setUserId(userId);
        pointsLog.setType(PointsTypeEnum.REFUND);
        pointsLog.setRefId(order.getId());
        pointsLog.setPoints(refundPoints);
        pointsLog.setRemark("取消订单：" + order.getOrderNo());
        pointsLog.setBeforePoints(beforePoints);
        pointsLog.setAfterPoints(afterPoints);
        pointsLog.setStatus(PointsStatusEnum.VALID);
        pointsLogMapper.insert(pointsLog);
    }

    @Override
    public OrderDetailResp getDetail(Long orderId) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 查询订单
        OrderDO order = orderMapper.selectById(orderId);
        CheckUtils.throwIf(null == order, "订单不存在");

        // 2. 校验是否为当前用户的订单
        CheckUtils.throwIf(!order.getCreateUser().equals(userId), "无权查看此订单");

        // 3. 查询商品信息
        ProductDO product = productMapper.selectById(order.getProductId());

        // 4. 组装返回数据
        OrderDetailResp resp = BeanUtil.copyProperties(order, OrderDetailResp.class);
        if (product != null) {
            // 这里可以设置商品名称等信息
        }
        return resp;
    }

    @Override
    public PageResp<OrderResp> page(OrderPageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 构建查询条件
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.eq("bo.create_user", userId);

        if (ObjectUtil.isNotNull(req.getStatus())) {
            wrapper.eq("bo.status", req.getStatus());
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
            wrapper.ge("bo.create_time", timeRange[0]).le("bo.create_time", timeRange[1]);
        }

        // 2. 排序
        if (ObjectUtil.isNotNull(req.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(req.getSortOrder());
            if ("createTime".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "bo.create_time");
            } else if ("costPoints".equals(req.getSortField())) {
                wrapper.orderBy(true, isAsc, "bo.cost_points");
            } else {
                wrapper.orderByDesc("bo.create_time");
            }
        } else {
            wrapper.orderByDesc("bo.create_time");
        }

        // 3. 分页查询（使用客户端专用的查询方法）
        Page<OrderDO> page = new Page<>(req.getPage(), req.getSize());
        IPage<OrderResp> pageResult = orderMapper.apiCustomPage(page, wrapper);

        return PageResp.build(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmCompletion(Long orderId) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 查询订单
        OrderDO order = orderMapper.selectById(orderId);
        CheckUtils.throwIf(null == order, "订单不存在");

        // 2. 校验订单状态是否为已交付
        CheckUtils.throwIf(order.getStatus() != OrderStatusEnum.DELIVERED, "只有已交付的订单才能确认完成");

        // 3. 校验是否为当前用户的订单
        CheckUtils.throwIf(!order.getCreateUser().equals(userId), "无权操作此订单");

        // 4. 更新订单状态为已完成
        order.setStatus(OrderStatusEnum.COMPLETED);
        order.setUpdateUser(userId);
        orderMapper.updateById(order);
    }
}
