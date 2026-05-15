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
 * 客户端活动图片响应
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "客户端活动图片响应")
public class ApiActivityImageResp {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 活动ID
     */
    @Schema(description = "活动ID", example = "1")
    private Long activityId;

    /**
     * 活动图片URL
     */
    @Schema(description = "活动图片URL", example = "https://example.com/images/activity1.jpg")
    private String imageUrl;

    /**
     * 图片内容描述
     */
    @Schema(description = "图片内容描述", example = "活动现场照片")
    private String content;

    private String createUser;
}
