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
 * 钉钉事件状态枚举
 *
 * @author weilai
 * @since 2026/01/19 15:30
 */
@Getter
public enum DingDingEventStatusEnum implements BaseEnum<String> {

    /**
     * 审批中
     */
    RUNNING("RUNNING", "审批中"),

    /**
     * 已撤销
     */
    TERMINATED("TERMINATED", "已撤销"),

    /**
     * 审批完成
     */
    COMPLETED("COMPLETED", "审批完成");

    private final String value;
    private final String description;

    DingDingEventStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
