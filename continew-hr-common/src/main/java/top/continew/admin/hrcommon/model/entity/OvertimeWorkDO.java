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

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 加班记录实体
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@TableName("biz_overtime_work")
public class OvertimeWorkDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 时长(小时)
     */
    private BigDecimal duration;

    /**
     * 转换积分数量
     */
    private Integer convertPoints;

    /**
     * 状态
     */
    private String status;

    private String result;
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 是否已删除（0：否；id：是）
     */
    private Long deleted;

    private String processInstanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
