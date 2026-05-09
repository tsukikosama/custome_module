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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import top.continew.admin.hrcommon.model.enums.ActivityTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动创建或修改参数
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Data
@Schema(description = "活动创建或修改参数")
public class ActivityReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题")
    @NotBlank(message = "活动标题不能为空")
    @Length(max = 64, message = "活动标题长度不能超过 {max} 个字符")
    private String title;

    /**
     * 首图
     */
    @Schema(description = "首图")
    @NotBlank(message = "首图不能为空")
    @Length(max = 128, message = "首图长度不能超过 {max} 个字符")
    private String image;

    /**
     * 活动内容
     */
    @Schema(description = "活动内容")
    @NotBlank(message = "活动内容不能为空")
    @Length(max = 65535, message = "活动内容长度不能超过 {max} 个字符")
    private String content;

    /**
     * 活动类型
     */
    @Schema(description = "活动类型")
    @NotNull(message = "活动类型不能为空")
    private ActivityTypeEnum type;

    /**
     * 活动开始时间
     */
    @Schema(description = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Schema(description = "活动结束时间")
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 活动人数（0代表不限制）
     */
    @Schema(description = "活动人数（0代表不限制）")
    @NotNull(message = "活动人数不能为空")
    private Integer activityPeopleNums;

    /**
     * 附件URL（支持文档、PPT、PDF等）
     */
    @Schema(description = "附件URL（支持文档、PPT、PDF等）")
    @Length(max = 500, message = "附件URL（支持文档、PPT、PDF等）长度不能超过 {max} 个字符")
    private String attachment;

    /**
     * 演讲人用户ID
     */
    @Schema(description = "演讲人用户ID")
    private Long speakerUserId;

    /**
     * 必须参加人用户ID列表
     */
    @Schema(description = "必须参加人用户ID列表")
    private String requireUserId;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean isTop;
}