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
import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.SuggestionTagEnum;

import java.io.Serial;
import java.time.*;

/**
 * 建议信息
 *
 * @author weilai
 * @since 2026/05/06 18:15
 */
@Data
@Schema(description = "建议信息")
public class SuggestionResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签
     */
    @Schema(description = "标签")
    private SuggestionTagEnum tag;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;
}