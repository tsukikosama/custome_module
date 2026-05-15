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

import top.continew.starter.extension.crud.model.entity.BaseIdDO;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 事件日志表详情信息
 *
 * @author weilai
 * @since 2026/01/19 11:42
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "事件日志表详情信息")
public class StreamEventDetailResp extends BaseIdDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 事件的id
     */
    @Schema(description = "事件的id")
    @ExcelProperty(value = "事件的id")
    private String eventId;

    /**
     * 事件类型
     */
    @Schema(description = "事件类型")
    @ExcelProperty(value = "事件类型")
    private String type;

    /**
     * 事件时间挫
     */
    @Schema(description = "事件时间挫")
    @ExcelProperty(value = "事件时间挫")
    private Long time;

    /**
     * 事件数据
     */
    @Schema(description = "事件数据")
    @ExcelProperty(value = "事件数据")
    private String content;

    @Schema(description = "事件流程id")
    @ExcelProperty(value = "事件流程id")
    private String processInstanceId;

    @ExcelProperty(value = "事件创建时间")
    private LocalDateTime createTime;
}
