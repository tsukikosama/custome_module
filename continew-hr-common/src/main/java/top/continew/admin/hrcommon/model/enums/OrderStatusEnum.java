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
 * 订单状态枚举
 *
 * @author weilai
 * @since 2026/01/15 12:00
 */
@Getter
public enum OrderStatusEnum implements BaseEnum<Integer> {

    /**
     * 待处理
     */
    PENDING(1, "待处理"),

    /**
     * 已采购
     */
    PURCHASED(2, "已采购"),

    /**
     * 已完成
     */
    COMPLETED(3, "已完成"),

    /**
     * 已取消
     */
    CANCELLED(4, "已取消"),

    /**
     * 已交付
     */
    DELIVERED(5, "已交付");

    private final Integer value;
    private final String description;

    OrderStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
