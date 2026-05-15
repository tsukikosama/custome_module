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

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;
import top.continew.starter.excel.converter.ExcelBaseEnumConverter;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "订单信息")
public class OrderResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    @ExcelProperty(value = "订单号", order = 1)
    private String orderNo;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名")
    @ExcelProperty(value = "商品名称", order = 3)
    private String productName;
    /**
     * 商品数量
     */
    @Schema(description = "商品数量")
    @ExcelProperty(value = "商品数量", order = 4)
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
    @ExcelProperty(value = "订单状态", converter = ExcelBaseEnumConverter.class, order = 5)
    private OrderStatusEnum status;

    /**
     * 下单时间
     */
    @Schema(description = "下单时间")
    @ExcelProperty(value = "下单时间", order = 8)
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

    /**
     * 下单用户
     */
    @Schema(description = "下单用户")
    @ExcelProperty(value = "下单用户", order = 7)
    private String customerName;

    private Integer points;

    /**
     * 花费的积分
     */
    @Schema(description = "花费的积分")
    @ExcelProperty(value = "花费积分", order = 6)
    private BigDecimal costPoints;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注", order = 9)
    private String remark;
}
