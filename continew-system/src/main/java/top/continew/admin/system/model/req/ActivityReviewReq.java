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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import top.continew.admin.hrcommon.model.enums.ActivityStatusEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 活动审核参数
 *
 * @author weilai
 * @since 2026/05/06
 */
@Data
@Schema(description = "活动审核参数")
public class ActivityReviewReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动 ID
     */
    @Schema(description = "活动 ID")
    @NotNull(message = "活动 ID 不能为空")
    private Long id;

    /**
     * 审核结果
     */
    @Schema(description = "审核结果")
    @NotNull(message = "审核结果不能为空")
    private ActivityStatusEnum status;

    /**
     * 审核备注
     */
    @Schema(description = "审核备注")
    @Length(max = 500, message = "审核备注长度不能超过 {max} 个字符")
    private String auditRemark;
}
