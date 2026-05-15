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
import java.io.Serializable;
import java.util.List;

/**
 * 活动参与用户自定义分页响应
 *
 * @author weilai
 * @since 2026/05/06
 */
@Data
@Schema(description = "活动参与用户自定义分页响应")
public class ActivityMemberCustomResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 演讲人用户ID列表
     */
    @Schema(description = "演讲人用户ID列表")
    private List<Long> speakerUserId;

    /**
     * 必参加人用户ID列表
     */
    @Schema(description = "必参加人用户ID列表")
    private List<Long> requireUserId;

    /**
     * 主动参加人用户ID列表
     */
    @Schema(description = "主动参加人用户ID列表")
    private List<Long> normalUserId;

    /**
     * 活动ID
     */
    @Schema(description = "活动ID")
    private Long activityId;

    /**
     * 活动标题
     */
    @Schema(description = "活动标题")
    private String activityTitle;
}
