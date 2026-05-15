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

import top.continew.admin.hrcommon.model.enums.ActivityStatusEnum;
import top.continew.admin.hrcommon.model.enums.ActivityTypeEnum;
import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;
import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 活动查询条件
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Data
@Schema(description = "活动查询条件")
public class ActivityQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题")
    @Query(type = QueryType.LIKE, columns = "ba.title")
    private String title;

    /**
     * 活动类型
     */
    @Schema(description = "活动类型")
    @Query(type = QueryType.EQ, columns = "ba.type")
    private ActivityTypeEnum type;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态")
    @Query(type = QueryType.EQ, columns = "ba.status")
    private ActivityStatusEnum status;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    @Query(type = QueryType.EQ, columns = "ba.is_top")
    private Boolean isTop;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Query(type = QueryType.BETWEEN, columns = "ba.create_time")
    private LocalDateTime[] createTime;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @Query(type = QueryType.EQ, columns = "ba.create_user")
    private Long createUser;
}