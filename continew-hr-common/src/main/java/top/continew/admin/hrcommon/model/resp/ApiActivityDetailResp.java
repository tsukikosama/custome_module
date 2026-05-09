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
import java.time.LocalDateTime;

/**
 * 客户端活动详情响应
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "客户端活动详情响应")
public class ApiActivityDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @Schema(description = "活动ID", example = "1")
    private Long id;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题", example = "技术分享会：Spring Cloud微服务架构实践")
    private String title;

    /**
     * 活动首图URL
     */
    @Schema(description = "活动首图URL", example = "https://example.com/images/activity1.jpg")
    private String image;

    /**
     * 活动内容
     */
    @Schema(description = "活动内容", example = "本次分享会将介绍Spring Cloud微服务架构的最佳实践")
    private String content;

    /**
     * 活动开始时间
     */
    @Schema(description = "活动开始时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-05-15 14:00:00")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Schema(description = "活动结束时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-05-15 17:00:00")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-04-29 10:00:00")
    private LocalDateTime createTime;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createUser;

    /**
     * 活动类型
     */
    @Schema(description = "活动类型（1普通活动/2分享会）", example = "2")
    private Integer type;

    /**
     * 活动人数（0表示不限制）
     */
    @Schema(description = "活动人数（0表示不限制）", example = "50")
    private Integer eventPeopleNums;

    /**
     * 活动附件URL
     */
    @Schema(description = "活动附件URL", example = "https://example.com/files/spring-cloud-presentation.pdf")
    private String attachment;

    /**
     * 举办人姓名
     */
    @Schema(description = "举办人姓名", example = "李四")
    private String speakerName;

    @Schema(description = "举办人id", example = "123")
    private String speakerId;

    /**
     * 必须参加人姓名列表（逗号分隔）
     */
    @Schema(description = "必须参加人姓名列表（逗号分隔）", example = "张三,李四")
    private String requireUser;

    /**
     * 必须参加人用户ID列表（逗号分隔）
     */
    @Schema(description = "必须参加人用户ID列表（逗号分隔）", example = "101,102")
    private String requireUserIds;

    /**
     * 主动参加人姓名列表（逗号分隔）
     */
    @Schema(description = "主动参加人姓名列表（逗号分隔）", example = "王五,赵六")
    private String normalUser;

    /**
     * 主动参加人用户ID列表（逗号分隔）
     */
    @Schema(description = "主动参加人用户ID列表（逗号分隔）", example = "101,102,103")
    private String normalUserIds;

    /**
     * 已报名人数统计
     */
    @Schema(description = "已报名人数统计", example = "25")
    private Integer enrolledCount;

}
