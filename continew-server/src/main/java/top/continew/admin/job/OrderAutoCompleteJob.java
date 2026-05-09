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

package top.continew.admin.job;

import cn.hutool.core.collection.CollUtil;
import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.common.log.SnailJobLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.hrcommon.mapper.OrderMapper;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;
import top.continew.admin.schedule.annotation.ConditionalOnEnabledScheduleJob;
import top.continew.starter.extension.tenant.annotation.TenantIgnore;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单自动完成任务
 *
 * <p>
 * 每天凌晨自动将7天前状态为"已交付"的订单更新为"已完成"
 * </p>
 *
 * @author weilai
 * @since 2026/05/09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAutoCompleteJob {

    private final OrderMapper orderMapper;

    /**
     * 定时自动完成订单（未启用 Snail Job 则使用它）
     */
    @Component
    @ConditionalOnProperty(prefix = "snail-job", name = "enabled", havingValue = "false")
    public static class Scheduler {

        private final OrderMapper orderMapper;

        public Scheduler(OrderMapper orderMapper) {
            this.orderMapper = orderMapper;
        }

        @TenantIgnore
        @Scheduled(cron = "0 0 0 * * ?")
        @Transactional(rollbackFor = Exception.class)
        public void autoCompleteOrder() {
            log.info("定时任务 [订单自动完成] 开始执行。");
            autoCompleteOrders(orderMapper);
            log.info("定时任务 [订单自动完成] 执行结束。");
        }
    }

    /**
     * 定时自动完成订单（启用 Snail Job 时）
     */
    @Component
    @ConditionalOnEnabledScheduleJob
    public static class ScheduleJob {

        private final OrderMapper orderMapper;

        public ScheduleJob(OrderMapper orderMapper) {
            this.orderMapper = orderMapper;
        }

        @TenantIgnore
        @JobExecutor(name = "OrderAutoCompleteJob")
        @Transactional(rollbackFor = Exception.class)
        public void autoCompleteOrder() {
            SnailJobLog.REMOTE.info("定时任务 [订单自动完成] 开始执行。");
            autoCompleteOrders(orderMapper);
            SnailJobLog.REMOTE.info("定时任务 [订单自动完成] 执行结束。");
        }
    }

    /**
     * 自动完成订单
     *
     * @param orderMapper 订单Mapper
     */
    private static void autoCompleteOrders(OrderMapper orderMapper) {
        // 计算7天前的时间
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 查询状态为"已交付"且更新时间在7天前的订单
        List<OrderDO> orders = orderMapper.lambdaQuery()
            .eq(OrderDO::getStatus, OrderStatusEnum.DELIVERED)
            .le(OrderDO::getUpdateTime, sevenDaysAgo)
            .list();

        if (CollUtil.isEmpty(orders)) {
            SnailJobLog.REMOTE.info("没有需要自动完成的订单。");
            return;
        }

        SnailJobLog.REMOTE.info("查询到 [{}] 个需要自动完成的订单。", orders.size());

        // 批量更新订单状态为"已完成"
        boolean success = orderMapper.lambdaUpdate()
            .set(OrderDO::getStatus, OrderStatusEnum.COMPLETED)
            .set(OrderDO::getFinishTime, LocalDateTime.now())
            .in(OrderDO::getId, orders.stream().map(OrderDO::getId).toList())
            .update();

        if (success) {
            SnailJobLog.REMOTE.info("成功将 [{}] 个订单自动完成。", orders.size());
        } else {
            SnailJobLog.REMOTE.error("订单自动完成失败。");
        }
    }
}
