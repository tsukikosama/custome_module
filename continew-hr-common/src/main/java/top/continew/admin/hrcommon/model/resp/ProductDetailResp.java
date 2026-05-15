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

package top.continew.admin.hrcommon.model.resp;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import top.continew.admin.common.base.model.resp.BaseDetailResp;
import java.io.Serial;
import java.time.*;

/**
 * 商品表详情信息
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "商品表详情信息")
public class ProductDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    @ExcelProperty(value = "商品名称")
    private String name;

    /**
     * 商品图片
     */
    @Schema(description = "商品图片")
    @ExcelProperty(value = "商品图片")
    private String image;

    /**
     * 商品积分
     */
    @Schema(description = "商品积分")
    @ExcelProperty(value = "商品积分")
    private Integer points;

    /**
     * 商品类型ID
     */
    @Schema(description = "商品类型ID")
    @ExcelProperty(value = "商品类型ID")
    private Long typeId;

    /**
     * 商品描述
     */
    @Schema(description = "商品描述")
    @ExcelProperty(value = "商品描述")
    private String description;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @ExcelProperty(value = "更新人")
    private Long updateUser;

    @Schema(description = "是否在架")
    @ExcelProperty(value = "是否在架")
    private Boolean isShelf;

    @Schema(description = "是否在架")
    @ExcelProperty(value = "月限数量")
    private Integer monthLimit;
}
