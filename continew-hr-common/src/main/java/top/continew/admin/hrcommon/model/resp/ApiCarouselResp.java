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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * 客户端轮播图响应
 *
 * @author weilai
 * @since 2026/05/09
 */
@Data
@Schema(description = "客户端轮播图响应")
public class ApiCarouselResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图ID
     */
    @Schema(description = "轮播图ID", example = "1")
    private Long id;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址（完整URL）", example = "https://example.com/images/banner1.jpg")
    private String url;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序（1、2、3...）", example = "1")
    private Integer sort;

    /**
     * 轮播图标题
     */
    @Schema(description = "轮播图标题", example = "新员工入职培训")
    private String title;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径（可为空）", example = "/pages/activity/detail/1")
    private String jumpPath;
}
