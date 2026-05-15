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
 * 轮播图创建或修改参数
 *
 * @author weilai
 * @since 2026/05/09 09:37
 */
@Data
@Schema(description = "轮播图创建或修改参数")
public class CarouselImageReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址")
    @NotBlank(message = "图片地址不能为空")
    @Length(max = 128, message = "图片地址长度不能超过 {max} 个字符")
    private String url;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    private Integer sort;

    /**
     * 轮播图标题
     */
    @Schema(description = "轮播图标题")
    @Length(max = 255, message = "轮播图标题长度不能超过 {max} 个字符")
    private String title;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径")
    @Length(max = 255, message = "跳转路径长度不能超过 {max} 个字符")
    private String jumpPath;

    private String content;
}