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

package top.continew.admin.system.model.req;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 订单状态日志创建或修改参数
 *
 * @author weilai
 * @since 2026/01/15 15:41
 */
@Data
@Schema(description = "订单状态日志创建或修改参数")
public class ProductOrderLogReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态", example = "1")
    private OrderStatusEnum status;

    /**
     * 更新之前订单状态
     */
    @Schema(description = "更新之前订单状态", example = "1")
    private OrderStatusEnum afterStatus;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @Length(max = 255, message = "备注长度不能超过 {max} 个字符")
    private String remark;
}