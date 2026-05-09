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

package top.continew.admin.system.model.req;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;
import top.continew.admin.hrcommon.model.enums.WishStatusEnum;
import java.io.Serial;
import java.io.Serializable;
import java.time.*;

/**
 * 心愿表创建或修改参数
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Data
@Schema(description = "心愿表创建或修改参数")
public class WishReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 50, message = "商品名称长度不能超过 {max} 个字符")
    private String name;

    /**
     * 父id（可选，如果不填则默认为0，表示是父心愿）
     */
    @Schema(description = "父id")
    private Long parentsId;

    /**
     * 许愿状态 1-心愿中 2-许愿成功 3-许愿失败 4-许愿取消
     */
    @Schema(description = "许愿状态")
    private WishStatusEnum status;

    /**
     * 失败原因
     */
    @Schema(description = "失败原因")
    private String failReason;
}