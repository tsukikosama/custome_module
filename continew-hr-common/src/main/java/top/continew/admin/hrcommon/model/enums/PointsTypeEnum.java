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

package top.continew.admin.hrcommon.model.enums;

import lombok.Getter;
import top.continew.starter.core.enums.BaseEnum;

/**
 * 积分类型枚举
 *
 * @author system
 * @since 2026/01/16
 */
@Getter
public enum PointsTypeEnum implements BaseEnum<Integer> {

    /**
     * 增加
     */
    INCREASE(1, "加班转换"),

    /**
     * 扣除
     */
    DECREASE(2, "兑换商品"),

    CLEAR(3, "积分失效"),

    REFUND(4, "积分退还"),

    ILLEGAL_DEDUCTION(5, "违规清除"),

    CORRECTION(6, "积分修正"),

    EVENT_POINT(7, "活动获得");

    private final Integer value;
    private final String description;

    PointsTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据数值判断积分类型
     *
     * @param points 积分数值
     * @return 积分类型枚举值
     */
    public static PointsTypeEnum getByValue(Integer points) {
        if (points == null) {
            throw new IllegalArgumentException("积分值不能为空");
        }
        return points > 0 ? INCREASE : DECREASE;
    }

    /**
     * 判断是否为减少类型
     *
     * @param type 积分类型枚举值
     * @return 是否为减少类型
     */
    public static boolean isDecrease(PointsTypeEnum type) {
        if (type == null) {
            return false;
        }
        return type == DECREASE || type == CLEAR || type == ILLEGAL_DEDUCTION;
    }
}
