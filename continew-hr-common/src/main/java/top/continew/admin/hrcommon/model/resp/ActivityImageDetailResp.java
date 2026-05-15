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

/**
 * 活动图片详情信息
 *
 * @author weilai
 * @since 2026/05/06 17:52
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "活动图片详情信息")
public class ActivityImageDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    @Schema(description = "事件id")
    @ExcelProperty(value = "事件id")
    private Long activityId;

    /**
     * 图片路径
     */
    @Schema(description = "图片路径")
    @ExcelProperty(value = "图片路径")
    private String imgUrl;

    /**
     * 图片内容描述
     */
    @Schema(description = "图片内容描述")
    @ExcelProperty(value = "图片内容描述")
    private String content;
}