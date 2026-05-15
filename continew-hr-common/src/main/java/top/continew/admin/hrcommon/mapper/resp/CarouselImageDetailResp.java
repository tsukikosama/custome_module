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

package top.continew.admin.hrcommon.mapper.resp;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import top.continew.admin.common.base.model.resp.BaseDetailResp;
import java.io.Serial;
import java.time.*;

/**
 * 轮播图详情信息
 *
 * @author weilai
 * @since 2026/05/09 09:37
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "轮播图详情信息")
public class CarouselImageDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址")
    @ExcelProperty(value = "图片地址")
    private String url;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    @ExcelProperty(value = "顺序")
    private Integer sort;

    /**
     * 轮播图标题
     */
    @Schema(description = "轮播图标题")
    @ExcelProperty(value = "轮播图标题")
    private String title;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径")
    @ExcelProperty(value = "跳转路径")
    private String jumpPath;

    @Schema(description = "内容")
    @ExcelProperty(value = "内容")
    private String content;
}