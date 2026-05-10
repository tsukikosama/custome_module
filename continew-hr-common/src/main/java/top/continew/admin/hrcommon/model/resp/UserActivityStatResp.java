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
import java.util.List;

/**
 * 用户活动统计响应
 *
 * @author weilai
 * @since 2026/05/10
 */
@Data
@Schema(description = "用户活动统计响应")
public class UserActivityStatResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名活动的数量
     */
    @Schema(description = "报名活动的数量", example = "3")
    private Integer activityCount;

    /**
     * 报名活动的ID集合（逗号分隔的字符串）
     */
    @Schema(description = "报名活动的ID集合", example = "1,5,8")
    private String activityIds;
}
