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
import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import java.io.Serial;
import java.time.*;

/**
 * 积分日志详情信息
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "积分日志详情信息")
public class PointsLogDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型")
    @ExcelProperty(value = "来源类型")
    private PointsTypeEnum type;

    /**
     * 来源id
     */
    @Schema(description = "来源id")
    @ExcelProperty(value = "来源id")
    private Long refId;

    /**
     * 积分数量
     */
    @Schema(description = "积分数量")
    @ExcelProperty(value = "积分数量")
    private Integer points;

    /**
     * 改变前数量
     */
    @Schema(description = "改变前数量")
    @ExcelProperty(value = "改变前数量")
    private Integer beforePoints;

    /**
     * 改变后数量
     */
    @Schema(description = "改变后数量")
    @ExcelProperty(value = "改变后数量")
    private Integer afterPoints;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 失效状态 1-有效 0-失效
     */
    @Schema(description = "失效状态")
    @ExcelProperty(value = "失效状态")
    private PointsStatusEnum status;

    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    @ExcelProperty(value = "失效时间")
    private LocalDateTime invalidTime;

}
