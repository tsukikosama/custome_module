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

import cn.hutool.core.util.IdUtil;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.hrcommon.model.entity.ProductDO;
import top.continew.admin.hrcommon.model.resp.OrderResp;
import top.continew.admin.hrcommon.model.enums.NoticeScopeEnum;
import top.continew.admin.hrcommon.model.enums.NoticeStatusEnum;
import top.continew.admin.hrcommon.mapper.OrderMapper;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.system.model.query.OrderQuery;
import top.continew.admin.hrcommon.model.req.NoticeReq;
import top.continew.admin.system.event.SendMessageEvent;
import top.continew.admin.system.model.req.OrderReq;
import top.continew.admin.system.model.req.ProductOrderLogReq;
import top.continew.admin.system.model.req.user.UserPointChangeReq;
import top.continew.admin.hrcommon.model.resp.OrderDetailResp;
import top.continew.admin.system.service.*;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.excel.util.ExcelUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.query.SortQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 订单业务实现
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderDO, OrderResp, OrderDetailResp, OrderQuery, OrderReq> implements OrderService {

    private final ProductService productService;
    private final ProductOrderLogService productOrderLogService;
    private final UserService userService;
    private final DingTalkApiService dingTalkApiService;
    private final NoticeService noticeService;
    private final ApplicationEventPublisher eventPublisher;
    private static final String HR_DEPT_ID = "1068728006";
    private static final Long ONE_EXCHANGE_ID = 806566015201706412L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(OrderReq req) {
        // 校验商品是否存在
        ProductDO product = productService.getById(req.getProductId());
        CheckUtils.throwIfNull(product, "商品不存在");
        CheckUtils.throwIf(!product.getIsShelf(), "商品已下架");

        //校验 用户是否还可以下单
        List<OrderDO> orderList = this.baseMapper.selectList(Wrappers.<OrderDO>lambdaQuery()
            .eq(OrderDO::getCreateUser, UserContextHolder.getUserId())
            .in(OrderDO::getStatus, List
                .of(OrderStatusEnum.PENDING, OrderStatusEnum.PURCHASED, OrderStatusEnum.COMPLETED))
            .eq(OrderDO::getProductId, req.getProductId()));

        // 计算当前商品已下单数量总和
        int totalOrderedQuantity = orderList.stream().mapToInt(OrderDO::getProductNum).sum();

        // 检查是否超过月限额
        if (product.getMonthLimit() != null) {
            int totalAfterOrder = totalOrderedQuantity + req.getProductNum();
            if (totalAfterOrder > product.getMonthLimit()) {
                int remainingQuantity = product.getMonthLimit() - totalOrderedQuantity;
                CheckUtils.throwIf(true, "下单失败，超过商品月限额。用户本月还能下单 " + Math.max(0, remainingQuantity) + " 个");
            }
        }

        // 校验用户积分是否足够
        UserDO currentUser = userService.getById(UserContextHolder.getUserId());
        CheckUtils.throwIfNull(currentUser, "用户不存在");
        CheckUtils.throwIfNull(currentUser.getPoints(), "用户积分信息不存在");

        // 创建订单
        OrderDO order = new OrderDO();
        order.setOrderNo("DD" + IdUtil.getSnowflakeNextId());
        order.setProductId(req.getProductId());
        order.setProductNum(req.getProductNum());
        // 计算所需积分
        Integer requiredPoints = product.getPoints() * req.getProductNum();
        CheckUtils.throwIf(currentUser.getPoints() < requiredPoints, "积分不足");
        // 设置花费的积分
        order.setCostPoints(BigDecimal.valueOf(requiredPoints));
        order.setStatus(OrderStatusEnum.PENDING);
        baseMapper.insert(order);

        //调用积分扣除的方法
        UserPointChangeReq userPointChangeReq = new UserPointChangeReq();
        userPointChangeReq.setUserId(currentUser.getId());
        userPointChangeReq.setPoints(requiredPoints);
        userPointChangeReq.setType(PointsTypeEnum.DECREASE);
        userPointChangeReq.setRefId(order.getId());
        //调用扣除积分的方法
        userService.changePoints(userPointChangeReq);

        // 创建订单日志
        ProductOrderLogReq logReq = new ProductOrderLogReq();
        logReq.setOrderId(order.getId());
        logReq.setStatus(OrderStatusEnum.PENDING);
        productOrderLogService.create(logReq);
        // 获取HR部门用户
        List<UserDO> userByDept = userService.getUserByDept(HR_DEPT_ID);
        // 发送钉钉待办任务通知
        try {

            if (userByDept != null && !userByDept.isEmpty()) {
                // 获取用户UnionId列表
                List<String> unionIds = userByDept.stream().map(UserDO::getUnionId).filter(Objects::nonNull).toList();

                if (!unionIds.isEmpty()) {
                    // 使用DingTalkApiService中的便捷方法创建简单的待办任务
                    String subject = "商品兑换通知";
                    String description = "用户" + currentUser.getNickname() + "兑换了" + req.getProductNum() + "个" + product
                        .getName() + "商品";
                    CreateTodoTaskRequest request = new CreateTodoTaskRequest();
                    request.setSubject(subject);
                    request.setDescription(description);
                    request.setParticipantIds(unionIds);
                    request.setExecutorIds(unionIds);
                    request.setIsOnlyShowExecutor(true);
                    //                    dingTalkApiService.sendMessage(request);

                }
            }
        } catch (Exception e) {
            // 记录错误日志，但不影响订单创建流程
            log.error("发送钉钉待办任务失败: {}", e.getMessage(), e);
        }
        NoticeReq noticeReq = new NoticeReq();
        //发送一个通知给人事
        noticeReq.setTitle("商品兑换通知");
        noticeReq.setContent("用户" + currentUser.getNickname() + "兑换了" + req.getProductNum() + "个" + product
            .getName() + "商品");
        noticeReq.setStatus(NoticeStatusEnum.PUBLISHED);
        noticeReq.setType("1");
        noticeReq.setNoticeScope(NoticeScopeEnum.USER);
        List<String> ids = userByDept.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
        noticeReq.setNoticeMethods(List.of(1));
        noticeReq.setNoticeUsers(ids);
        noticeReq.setIsTiming(false);
        noticeService.create(noticeReq);
        return order.getId();
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        //获取到订单的记录
        OrderDO order = getById(orderId);
        CheckUtils.throwIfNotEqual(order.getStatus(), OrderStatusEnum.PENDING, "订单状态不是待处理无法取消");
        // 创建更新请求对象
        OrderReq updateReq = new OrderReq();
        updateReq.setStatus(OrderStatusEnum.CANCELLED);
        // 更新订单状态
        update(updateReq, orderId);

        //进行积分退还
        UserPointChangeReq req = new UserPointChangeReq();
        req.setType(PointsTypeEnum.REFUND);
        req.setUserId(order.getCreateUser());
        req.setPoints(order.getProductNum() * productService.getById(order.getProductId()).getPoints());
        userService.changePoints(req);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(OrderReq req, Long id) {
        // 获取当前订单记录
        OrderDO existingOrder = getById(id);
        CheckUtils.throwIfNull(existingOrder, "订单不存在");
        CheckUtils.throwIf(existingOrder.getStatus().equals(OrderStatusEnum.CANCELLED), "订单已取消无法修改");
        UserDO userDO = userService.getById(existingOrder.getCreateUser());

        // 判断订单状态是否有改变
        if (req.getStatus() != null && !req.getStatus().equals(existingOrder.getStatus())) {
            List<UserDO> hrList = userService.getUserListForPushMessage();
            CheckUtils.throwIf(req.getStatus().equals(OrderStatusEnum.CANCELLED) && !existingOrder.getStatus()
                .equals(OrderStatusEnum.PENDING), "订单状态不是待处理无法取消");
            // 状态发生改变，创建订单日志记录状态变更
            ProductOrderLogReq logReq = new ProductOrderLogReq();
            logReq.setOrderId(id);
            logReq.setStatus(req.getStatus());
            logReq.setAfterStatus(existingOrder.getStatus());
            productOrderLogService.create(logReq);
            //如果状态发送了改变 推送一个通知给指定的用户
            NoticeReq noticeReq = new NoticeReq();
            //发送一个通知给人事
            noticeReq.setTitle("订单状态更新");
            noticeReq.setContent(String.format("用户%s您兑换的订单%d状态更新为%s", userDO.getNickname(), id, req.getStatus()
                .getDescription()));
            noticeReq.setStatus(NoticeStatusEnum.PUBLISHED);
            noticeReq.setType("1");
            noticeReq.setNoticeScope(NoticeScopeEnum.USER);
            noticeReq.setNoticeMethods(List.of(1));
            List<String> userIds = Stream.concat(Stream.of(userDO.getId().toString()), hrList.stream()
                .map(UserDO::getId)
                .map(String::valueOf)).collect(Collectors.toList());
            noticeReq.setNoticeUsers(userIds);
            noticeReq.setIsTiming(false);
            noticeService.create(noticeReq);

            // 发布消息事件，触发钉钉推送（异步执行）
            ProductDO product = productService.getById(existingOrder.getProductId());
            String content = String.format("订单状态更新通知\n订单号：%s\n状态变更：%s → %s\n商品：%s\n数量：%d", existingOrder
                .getOrderNo(), existingOrder.getStatus().getDescription(), req.getStatus()
                    .getDescription(), product != null ? product.getName() : "未知商品", existingOrder.getProductNum());
            SendMessageEvent event = new SendMessageEvent(this, hrList, content, true);
            eventPublisher.publishEvent(event);

        }

        super.update(req, id);
    }

    @Override
    public PageResp<OrderResp> page(OrderQuery query, PageQuery pageQuery) {
        QueryWrapper<OrderDO> wrapper = this.buildQueryWrapper(query);
        wrapper.eq("bo.deleted", 0);
        IPage<OrderResp> page = this.baseMapper.customPage(new Page((long)pageQuery.getPage(), (long)pageQuery
            .getSize()), wrapper);
        return PageResp.build(page);
    }

    @Override
    public void export(OrderQuery query, SortQuery sortQuery, HttpServletResponse response) {
        QueryWrapper<OrderDO> wrapper = this.buildQueryWrapper(query);
        wrapper.eq("bo.deleted", 0);
        List<OrderResp> list = this.baseMapper.customList(wrapper);
        ExcelUtils.export(list, "订单数据", OrderResp.class, response);
    }

}