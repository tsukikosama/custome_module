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

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.continew.admin.common.base.model.entity.BaseCreateDO;
import top.continew.admin.hrcommon.model.enums.SuggestionTagEnum;

import java.io.Serial;

/**
 * 建议实体
 *
 * @author weilai
 * @since 2026/05/06 18:15
 */
@Data
@TableName("biz_suggestion")
public class SuggestionDO extends BaseCreateDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签
     */
    private SuggestionTagEnum tag;

    /**
     * 内容
     */
    private String content;
}
