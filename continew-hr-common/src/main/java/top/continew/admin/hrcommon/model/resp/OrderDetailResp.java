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

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import top.continew.admin.common.base.model.resp.BaseDetailResp;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情信息
 *
 * @author weilai
 * @since 2026/01/15 16:05
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "订单详情信息")
public class OrderDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    @ExcelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID")
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 商品数量
     */
    @Schema(description = "商品数量")
    @ExcelProperty(value = "商品数量")
    private Integer productNum;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    @ExcelProperty(value = "订单状态")
    private OrderStatusEnum status;

    /**
     * 花费的积分
     */
    @Schema(description = "花费的积分")
    @ExcelProperty(value = "花费的积分")
    private BigDecimal costPoints;

    /**
     * 取消时间
     */
    @Schema(description = "取消时间")
    @ExcelProperty(value = "取消时间")
    private LocalDateTime cancelTime;

    /**
     * 完成时间
     */
    @Schema(description = "完成时间")
    @ExcelProperty(value = "完成时间")
    private LocalDateTime finishTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注")
    private String remark;
}
