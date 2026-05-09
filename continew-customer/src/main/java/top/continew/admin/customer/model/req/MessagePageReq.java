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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;

/**
 * 消息分页查询请求
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "消息分页查询请求")
public class MessagePageReq {

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
    @Max(value = 100, message = "每页数量不能超过100")
    private Integer size = 10;

    /**
     * 消息类型（1系统消息/2安全消息），不传则查询所有类型
     */
    @Schema(description = "消息类型（1系统消息/2安全消息）", example = "1")
    private Integer type;

    /**
     * 排序字段（createTime创建时间/type消息类型）
     */
    @Schema(description = "排序字段", example = "createTime")
    private String sortField = "createTime";

    /**
     * 排序方式（asc升序/desc降序）
     */
    @Schema(description = "排序方式", example = "desc")
    private String sortOrder = "desc";
}
