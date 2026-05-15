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
import top.continew.admin.hrcommon.model.enums.ActivityMemberType;

import java.io.Serial;

/**
 * 活动参与用户信息
 *
 * @author weilai
 * @since 2026/05/06 11:03
 */
@Data
@Schema(description = "活动参与用户信息")
public class ActivityMemberResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @Schema(description = "活动id")
    private Long activityId;

    /**
     * 是否已删除（0：否；id：是）
     */
    @Schema(description = "是否已删除（0：否；id：是）")
    private Long deleted;

    /**
     * 参加人员id
     */
    @Schema(description = "参加人员id")
    private Long userId;

    /**
     * 参加成员类型（1：演讲人；2：必参加人；3：主动参加）
     */
    @Schema(description = "参加成员类型（1：演讲人；2：必参加人；3：主动参加）")
    private ActivityMemberType type;

    /**
     * 状态（1：已报名；2：已取消）
     */
    @Schema(description = "状态（1：已报名；2：已取消）")
    private Integer status;
}