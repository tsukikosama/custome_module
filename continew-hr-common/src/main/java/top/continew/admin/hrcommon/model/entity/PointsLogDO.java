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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import top.continew.admin.hrcommon.model.enums.PointsStatusEnum;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.starter.extension.crud.model.entity.BaseIdDO;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 积分日志实体
 *
 * @author weilai
 * @since 2026/01/16 14:18
 */
@Data
@TableName("biz_points_log")
public class PointsLogDO extends BaseIdDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 来源类型
     */
    private PointsTypeEnum type;

    /**
     * 来源id
     */
    private Long refId;

    /**
     * 积分数量
     */
    private Integer points;

    /**
     * 改变前数量
     */
    private Integer beforePoints;

    /**
     * 改变后数量
     */
    private Integer afterPoints;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已删除（0：否；id：是）
     */
    private Long deleted;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 失效状态 1-有效 0-失效
     */
    private PointsStatusEnum status;

    /**
     * 失效时间
     */
    private LocalDateTime invalidTime;
}
