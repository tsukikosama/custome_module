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

import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;
import java.util.List;

/**
 * 积分日志创建或修改参数
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@Schema(description = "积分日志创建或修改参数")
public class PointsLogReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private List<Long> userIds;

    @Schema(description = "积分")
    private Integer points;

    @Schema(description = "积分类型")
    private PointsTypeEnum type;

    @Schema(description = "变化之后的积分")
    private Integer afterPoints;

    @Schema(description = "变化之前的积分")
    private Integer beforePoints;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "失效状态 1-有效 0-失效")
    private PointsStatusEnum status;

    @Schema(description = "失效时间")
    private LocalDateTime invalidTime;
}