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
import top.continew.admin.hrcommon.model.enums.OrderStatusEnum;

import java.time.LocalDateTime;

/**
 * 订单状态日志信息
 *
 * @author weilai
 * @since 2026/01/16 10:00
 */
@Data
@Schema(description = "订单状态日志信息")
public class OrderLogResp {

    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private OrderStatusEnum status;

    /**
     * 订单更新之前的状态
     */
    @Schema(description = "订单更新之前的状态")
    private OrderStatusEnum afterStatus;

    private String createUser;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
