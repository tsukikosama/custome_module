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
 * 许愿状态枚举
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Getter
public enum WishStatusEnum implements BaseEnum<Integer> {

    /**
     * 心愿中
     */
    IN_PROGRESS(1, "心愿中"),

    /**
     * 许愿成功
     */
    SUCCESS(2, "许愿成功"),

    /**
     * 许愿失败
     */
    FAILED(3, "许愿失败"),

    /**
     * 许愿取消
     */
    CANCELLED(4, "许愿取消");

    private final Integer value;
    private final String description;

    WishStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
