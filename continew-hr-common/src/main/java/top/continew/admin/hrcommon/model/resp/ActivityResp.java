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
import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.ActivityStatusEnum;
import top.continew.admin.hrcommon.model.enums.ActivityTypeEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 活动信息
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Data
@Schema(description = "活动信息")
public class ActivityResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题")
    private String title;

    /**
     * 首图
     */
    @Schema(description = "首图")
    private String image;

    /**
     * 活动内容
     */
    @Schema(description = "活动内容")
    private String content;

    /**
     * 活动类型
     */
    @Schema(description = "活动类型")
    private ActivityTypeEnum type;

    /**
     * 活动开始时间
     */
    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    /**
     * 活动人数（0代表不限制）
     */
    @Schema(description = "活动人数（0代表不限制）")
    private Integer activityPeopleNums;

    /**
     * 附件URL（支持文档、PPT、PDF等）
     */
    @Schema(description = "附件URL（支持文档、PPT、PDF等）")
    private String attachment;

    /**
     * 演讲人用户ID列表
     */
    @Schema(description = "演讲人用户ID列表")
    private String speakerUserId;

    /**
     * 必须参加人用户ID列表
     */
    @Schema(description = "必须参加人用户ID列表")
    private String requireUserId;

    /**
     * 主动参加人用户ID列表
     */
    @Schema(description = "主动参加人用户ID列表")
    private String normalUserId;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态")
    private ActivityStatusEnum status;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean isTop;

    /**
     * 审核备注
     */
    @Schema(description = "审核备注")
    private String auditRemark;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @Schema(description = "审核人ID")
    private Long auditUser;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private Long updateUser;

    /**
     * 是否已删除（0：否；id：是）
     */
    @Schema(description = "是否已删除（0：否；id：是）")
    private Long deleted;

    /**
     * 参与人数（演讲人 + 必须参与的人）
     */
    @Schema(description = "参与人数（演讲人 + 必须参与的人）")
    private Integer participantCount;

}