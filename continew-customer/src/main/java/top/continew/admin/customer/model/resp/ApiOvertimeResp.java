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

package top.continew.admin.customer.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户端加班记录响应
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "客户端加班记录响应")
public class ApiOvertimeResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 加班记录ID
     */
    @Schema(description = "加班记录ID", example = "1")
    private Long id;

    /**
     * 加班日期
     */
    @Schema(description = "加班日期（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-04-29 18:00:00")
    private LocalDateTime overtimeDate;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间（格式：HH:mm:ss）", example = "18:00:00")
    private String startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间（格式：HH:mm:ss）", example = "22:00:00")
    private String endTime;

    /**
     * 加班时长
     */
    @Schema(description = "加班时长（小时）", example = "4.00")
    private BigDecimal duration;

    /**
     * 获得的积分
     */
    @Schema(description = "获得的积分", example = "40.00")
    private Integer points;

    /**
     * 状态
     */
    @Schema(description = "状态（1待审核/2已通过/3已拒绝）", example = "2")
    private Integer status;

    /**
     * 状态文本描述
     */
    @Schema(description = "状态文本描述", example = "已通过")
    private String statusText;

    /**
     * 结果
     */
    @Schema(description = "结果（1有效/2无效）", example = "1")
    private Integer result;

    /**
     * 结果文本描述
     */
    @Schema(description = "结果文本描述", example = "有效")
    private String resultText;

    /**
     * 备注说明
     */
    @Schema(description = "备注说明", example = "项目紧急加班")
    private String remark;

    /**
     * 审核备注
     */
    @Schema(description = "审核备注", example = "审核通过")
    private String auditRemark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-04-29 10:00:00")
    private String createTime;
}
