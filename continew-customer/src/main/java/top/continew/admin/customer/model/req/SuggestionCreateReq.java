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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新增建议请求
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "新增建议请求")
public class SuggestionCreateReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 建议标签（1工作诉求/2感情宣泄/3吐槽建议/4生活碎片）
     */
    @Schema(description = "建议标签", example = "1")
    @NotNull(message = "建议标签不能为空")
    private Integer tag;

    /**
     * 建议内容
     */
    @Schema(description = "建议内容", example = "希望增加一个商品收藏功能，方便收藏感兴趣的商品")
    @NotBlank(message = "建议内容不能为空")
    private String content;
}
