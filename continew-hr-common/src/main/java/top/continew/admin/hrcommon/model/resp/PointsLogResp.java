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

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.starter.excel.converter.ExcelBaseEnumConverter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 积分日志信息
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "积分日志信息")
public class PointsLogResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名")
    @ExcelProperty(value = "用户姓名", order = 1)
    private String name;
    /**
     * 类型 1增加 2扣除
     */
    @Schema(description = "类型 1增加 2扣除")
    @ExcelProperty(value = "积分类型", converter = ExcelBaseEnumConverter.class, order = 3)
    private PointsTypeEnum type;

    /**
     * 积分数量
     */
    @Schema(description = "积分数量")
    @ExcelProperty(value = "积分数量", order = 4)
    private Integer points;

    /**
     * 改变前数量
     */
    @Schema(description = "改变前数量")
    @ExcelProperty(value = "改变前积分", order = 5)
    private Integer beforePoints;

    /**
     * 改变后数量
     */
    @Schema(description = "改变后数量")
    @ExcelProperty(value = "改变后积分", order = 6)
    private Integer afterPoints;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注", order = 8)
    private String remark;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @ExcelProperty(value = "部门", order = 2)
    private String deptName;

    /**
     * 失效状态 1-有效 0-失效
     */
    @Schema(description = "失效状态")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class, order = 7)
    private PointsStatusEnum status;

    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    private LocalDateTime invalidTime;
}
