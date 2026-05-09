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

package top.continew.admin.customer.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品查询条件
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Data
@Schema(description = "商品查询条件")
public class ApiProductQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称（模糊查询）
     */
    @Schema(description = "商品名称（模糊查询）", example = "键盘")
    private String name;

    /**
     * 商品类型ID
     */
    @Schema(description = "商品类型ID", example = "1")
    private Long typeId;

    /**
     * 排序字段（points积分/time创建时间）
     */
    @Schema(description = "排序字段（points积分/time创建时间）", example = "points")
    private String sortField;

    /**
     * 排序方式（asc升序/desc降序）
     */
    @Schema(description = "排序方式（asc升序/desc降序）", example = "desc")
    private String sortOrder;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @NotNull(message = "当前页码不能为空")
    @Min(value = 1, message = "当前页码必须大于0")
    private Integer page;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer size;
}
