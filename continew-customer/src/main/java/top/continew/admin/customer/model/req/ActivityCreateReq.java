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

package top.continew.admin.customer.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建活动请求
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "创建活动请求")
public class ActivityCreateReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题", example = "技术分享会：Spring Cloud微服务架构实践")
    @NotBlank(message = "活动标题不能为空")
    private String title;

    /**
     * 活动内容
     */
    @Schema(description = "活动内容", example = "本次分享会将介绍Spring Cloud微服务架构的最佳实践")
    @NotBlank(message = "活动内容不能为空")
    private String content;

    /**
     * 活动首图URL
     */
    @Schema(description = "活动首图URL", example = "https://example.com/images/activity1.jpg")
    @NotBlank(message = "活动首图不能为空")
    private String image;

    /**
     * 活动人数限制（0表示不限制）
     */
    @Schema(description = "活动人数限制（0表示不限制）", example = "50")
    @NotNull(message = "活动人数不能为空")
    private Integer eventPeopleNums;

    /**
     * 活动附件URL（支持文档、PPT、PDF等）
     */
    @Schema(description = "活动附件URL", example = "https://example.com/files/spring-cloud-presentation.pdf")
    private String attachment;

    /**
     * 活动开始时间
     */
    @Schema(description = "活动开始时间", example = "2026-05-15 14:00:00")
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Schema(description = "活动结束时间", example = "2026-05-15 17:00:00")
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 必须参加的人员ID列表
     */
    @Schema(description = "必须参加的人员ID列表", example = "[10, 20, 30]")
    private List<Long> requiredMemberIds;
}
