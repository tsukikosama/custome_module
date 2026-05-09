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

package top.continew.admin.hrcommon.model.resp;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.*;

/**
 * 订单信息
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Data
@Schema(description = "订单信息")
public class OrderResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String orderNo;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名")
    private String productName;
    /**
     * 商品数量
     */
    @Schema(description = "商品数量")
    private Integer productNum;

    /**
     * 下单用户ID
     */
    @Schema(description = "下单用户ID")
    private Long userId;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private OrderStatusEnum status;

    /**
     * 下单时间
     */
    @Schema(description = "下单时间")
    private LocalDateTime orderTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private Long updateUser;

    /**
     * 创建人
     */
    @Schema(description = "更新人", example = "超级管理员")
    private String updateUserString;

    private String customerName;

    private Integer points;

    /**
     * 花费的积分
     */
    @Schema(description = "花费的积分")
    private BigDecimal costPoints;
}
