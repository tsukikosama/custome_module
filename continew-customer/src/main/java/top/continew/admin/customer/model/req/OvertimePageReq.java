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

package top.continew.admin.customer.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 加班记录分页查询请求
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "加班记录分页查询请求")
public class OvertimePageReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "当前页码必须大于0")
    private Integer page = 1;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10")
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer size = 10;

    /**
     * 状态筛选（1待审核/2已通过/3已拒绝）
     */
    @Schema(description = "状态筛选（1待审核/2已通过/3已拒绝）", example = "2")
    private Integer status;

    /**
     * 结果筛选（1有效/2无效）
     */
    @Schema(description = "结果筛选（1有效/2无效）", example = "1")
    private Integer result;

    /**
     * 创建时间范围数组，第一个是开始时间，第二个是结束时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "时间范围数组", example = "[\"2026-04-01 00:00:00\", \"2026-04-30 23:59:59\"]")
    private LocalDateTime[] times;

    /**
     * 开始时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "开始时间", example = "2026-04-01 00:00:00")
    private LocalDateTime startTime;

    /**
     * 结束时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "结束时间", example = "2026-04-30 23:59:59")
    private LocalDateTime endTime;

    /**
     * 排序字段（overtimeDate加班日期/createTime创建时间/points积分）
     */
    @Schema(description = "排序字段", example = "createTime")
    private String sortField;

    /**
     * 排序方式（asc升序/desc降序）
     */
    @Schema(description = "排序方式", example = "desc")
    private String sortOrder;
}
