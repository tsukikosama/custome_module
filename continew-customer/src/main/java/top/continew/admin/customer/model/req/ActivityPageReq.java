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

/**
 * 活动分页查询请求
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "活动分页查询请求")
public class ActivityPageReq {

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
     * 排序字段（startTime活动开始时间/endTime活动结束时间/createTime创建时间）
     */
    @Schema(description = "排序字段（startTime活动开始时间/endTime活动结束时间/createTime创建时间）", example = "startTime")
    private String sortField;

    /**
     * 排序方式（asc升序/desc降序）
     */
    @Schema(description = "排序方式（asc升序/desc降序）", example = "desc")
    private String sortOrder;

    /**
     * 活动类型（1普通活动/2分享会，不传则查询所有）
     */
    @Schema(description = "活动类型（1普通活动/2分享会，不传则查询所有）", example = "2")
    private Integer type;
}
