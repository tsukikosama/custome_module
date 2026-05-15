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

package top.continew.admin.customer.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户端积分日志信息
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@Schema(description = "客户端积分日志信息")
public class ApiPointsLogResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @Schema(description = "日志ID")
    private Long id;

    /**
     * 积分变动时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "积分变动时间")
    private String time;

    /**
     * 积分类型（1加班转换/2兑换商品/3积分失效/4积分退还/5违规清除/6积分修正/7活动获得）
     */
    @Schema(description = "积分类型")
    private Integer type;

    /**
     * 积分类型文本描述
     */
    @Schema(description = "积分类型文本描述")
    private String typeText;

    /**
     * 积分变动数量（正数表示增加，负数表示扣除）
     */
    @Schema(description = "积分变动数量")
    private BigDecimal amount;

    /**
     * 备注说明
     */
    @Schema(description = "备注说明")
    private String remark;
}
