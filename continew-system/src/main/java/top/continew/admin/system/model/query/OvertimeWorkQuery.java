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

import top.continew.admin.hrcommon.model.enums.DingDingEventResultEnum;
import top.continew.admin.hrcommon.model.enums.DingDingEventStatusEnum;
import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;
import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 加班记录查询条件
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "加班记录查询条件")
public class OvertimeWorkQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Query(type = QueryType.EQ,columns = "bow.user_id")
    private Long userId;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Query(type = QueryType.EQ, columns = "bow.status")
    private DingDingEventStatusEnum status;

    @Schema(description = "结果")
    @Query(type = QueryType.EQ,columns = "bow.result")
    private DingDingEventResultEnum result;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Query(type = QueryType.BETWEEN, columns = "bow.create_time")
    private LocalDateTime[] createTime;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @Query(type = QueryType.EQ)
    private Long createUser;

    private String processInstanceId;
}