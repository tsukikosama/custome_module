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
 * 积分状态枚举
 *
 * @author weilai
 * @since 2026/05/06
 */
@Getter
public enum PointsStatusEnum implements BaseEnum<Integer> {

    /**
     * 失效
     */
    INVALID(0, "失效"),

    /**
     * 有效
     */
    VALID(1, "有效");

    private final Integer value;
    private final String description;

    PointsStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
