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

package top.continew.admin.hrcommon.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import top.continew.admin.common.base.model.entity.BaseDO;
import top.continew.admin.hrcommon.model.enums.ActivityStatusEnum;
import top.continew.admin.hrcommon.model.enums.ActivityTypeEnum;

import java.io.Serial;
import java.time.*;

/**
 * 活动实体
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Data
@TableName("biz_activity")
public class ActivityDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 首图
     */
    private String image;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 活动类型
     */
    private ActivityTypeEnum type;

    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 活动人数（0代表不限制）
     */
    private Integer activityPeopleNums;

    /**
     * 附件URL（支持文档、PPT、PDF等）
     */
    private String attachment;

    /**
     * 审批状态
     */
    private ActivityStatusEnum status;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    private Long auditUser;

    /**
     * 是否已删除（0：否；id：是）
     */
    private Long deleted;
}
