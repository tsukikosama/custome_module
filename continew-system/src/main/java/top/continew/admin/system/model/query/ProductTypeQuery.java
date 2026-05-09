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

import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;
import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 商品分类查询条件
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Data
@Schema(description = "商品分类查询条件")
public class ProductTypeQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品类型名称
     */
    @Schema(description = "商品类型名称")
    @Query(type = QueryType.LIKE)
    private String name;
}