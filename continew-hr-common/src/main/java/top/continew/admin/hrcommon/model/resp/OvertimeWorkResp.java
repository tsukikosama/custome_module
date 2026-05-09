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

import cn.crane4j.annotation.Assemble;
import cn.crane4j.annotation.Mapping;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.common.constant.ContainerConstants;

import java.io.Serial;
import java.time.*;
import java.math.BigDecimal;

/**
 * 加班记录信息
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "加班记录信息")
public class OvertimeWorkResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Assemble(container = ContainerConstants.USER_NICKNAME, props = @Mapping(ref = "userName"))
    private Long userId;

    private String nickname;

    /**
     * 时长(小时)
     */
    @Schema(description = "时长(小时)")
    private BigDecimal duration;

    /**
     * 转换积分数量
     */
    @Schema(description = "转换积分数量")
    private Integer convertPoints;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;

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

    private String processInstanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String result;
}
