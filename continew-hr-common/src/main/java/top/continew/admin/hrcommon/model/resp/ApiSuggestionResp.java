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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端建议信息
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "客户端建议信息")
public class ApiSuggestionResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 建议ID
     */
    @Schema(description = "建议ID")
    private Long id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 建议标签
     */
    @Schema(description = "建议标签")
    private Integer tag;

    /**
     * 建议内容
     */
    @Schema(description = "建议内容")
    private String content;
}
