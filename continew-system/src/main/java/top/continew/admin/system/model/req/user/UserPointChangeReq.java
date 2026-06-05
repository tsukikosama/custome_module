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

package top.continew.admin.system.model.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.io.Serial;

/**
 * 用户积分变更请求
 *
 * @author weilai
 * @since 2026/01/16 10:00
 */
@Data
@Schema(description = "用户积分变更请求")
public class UserPointChangeReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 积分数量（正数为增加，负数为扣除）
     */
    @Schema(description = "积分数量", example = "10")
    @NotNull(message = "积分数量不能为空")
    private Integer points;

    @Schema(description = "变更类型", example = "1")
    @NotNull(message = "变更类型不能为空")
    private PointsTypeEnum type;

    /**
     * 来源id
     */
    @Schema(description = "来源id")
    private Long refId;

    private String remark;
}