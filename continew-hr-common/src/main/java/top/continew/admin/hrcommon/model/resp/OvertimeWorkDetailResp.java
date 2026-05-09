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

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import top.continew.admin.common.base.model.resp.BaseDetailResp;
import java.io.Serial;
import java.time.*;
import java.math.BigDecimal;

/**
 * 加班记录详情信息
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "加班记录详情信息")
public class OvertimeWorkDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 时长(小时)
     */
    @Schema(description = "时长(小时)")
    @ExcelProperty(value = "时长(小时)")
    private BigDecimal duration;

    /**
     * 转换积分数量
     */
    @Schema(description = "转换积分数量")
    @ExcelProperty(value = "转换积分数量")
    private Integer convertPoints;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    @ExcelProperty(value = "租户ID")
    private Long tenantId;

    /**
     * 是否已删除（0：否；id：是）
     */
    @Schema(description = "是否已删除（0：否；id：是）")
    @ExcelProperty(value = "是否已删除（0：否；id：是）")
    private Long deleted;

    private String processInstanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String result;
}
