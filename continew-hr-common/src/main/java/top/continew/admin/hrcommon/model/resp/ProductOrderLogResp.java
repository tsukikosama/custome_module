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
import java.time.*;

/**
 * 订单状态日志信息
 *
 * @author weilai
 * @since 2026/01/15 15:41
 */
@Data
@Schema(description = "订单状态日志信息")
public class ProductOrderLogResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private OrderStatusEnum status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
