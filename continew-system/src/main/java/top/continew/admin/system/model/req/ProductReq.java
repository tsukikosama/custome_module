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

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 商品表创建或修改参数
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
@Data
@Schema(description = "商品表创建或修改参数")
public class ProductReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 50, message = "商品名称长度不能超过 {max} 个字符")
    private String name;

    /**
     * 商品积分
     */
    @Schema(description = "商品积分")
    @NotNull(message = "商品积分不能为空")
    private Integer points;

    /**
     * 商品类型ID
     */
    @Schema(description = "商品类型ID")
    @NotNull(message = "商品类型ID不能为空")
    private Long typeId;

    /**
     * 商品图片
     */
    @Schema(description = "商品图片")
    private String image;

    /**
     * 商品描述
     */
    @Schema(description = "商品描述")
    @Length(max = 65535, message = "商品描述长度不能超过 {max} 个字符")
    private String description;

    @Schema(description = "是否在架")
    private Boolean isShelf;

    @Schema(description = "是否在架")
    @Min(value = 0, message = "月限购数量不能小于 {value}")
    @NotNull(message = "月限购数量不能为空")
    private Integer monthLimit;
}