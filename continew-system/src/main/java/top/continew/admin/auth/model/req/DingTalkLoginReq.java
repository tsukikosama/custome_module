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

package top.continew.admin.auth.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 钉钉登录请求参数
 *
 * @author Continew
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "钉钉登录请求参数")
public class DingTalkLoginReq extends LoginReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 钉钉授权码
     */
    @Schema(description = "钉钉授权码", example = "abc123")
    @NotBlank(message = "授权码不能为空")
    private String authCode;

    /**
     * 钉钉用户手机号
     */
    @Schema(description = "钉钉用户手机号", example = "13800138000")
    private String phone;

    /**
     * 钉钉用户昵称
     */
    @Schema(description = "钉钉用户昵称", example = "张三")
    private String nickname;

    /**
     * 钉钉用户UnionID
     */
    @Schema(description = "钉钉用户UnionID", example = "ding123456")
    private String unionId;
}