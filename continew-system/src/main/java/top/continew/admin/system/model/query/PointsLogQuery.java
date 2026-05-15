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

package top.continew.admin.system.model.query;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;
import java.io.Serial;
import java.io.Serializable;
import java.time.*;
import java.util.List;

/**
 * 积分日志查询条件
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@Schema(description = "积分日志查询条件")
public class PointsLogQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Query(type = QueryType.IN)
    private List<Long> userId;

    @Query(type = QueryType.EQ, columns = "su.dept_id")
    private Long deptId;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型")
    @Query(type = QueryType.EQ)
    private PointsTypeEnum type;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Query(type = QueryType.BETWEEN, columns = "bpl.create_time")
    private LocalDateTime[] createTime;

    /**
     * 失效状态
     */
    @Schema(description = "失效状态")
    @Query(type = QueryType.EQ, columns = "bpl.status")
    private PointsStatusEnum status;

    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    @Query(type = QueryType.BETWEEN, columns = "bpl.invalid_time")
    private LocalDateTime[] invalidTime;
}