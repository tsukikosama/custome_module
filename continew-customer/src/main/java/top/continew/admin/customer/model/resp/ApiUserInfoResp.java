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

package top.continew.admin.customer.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * 客户端用户信息响应
 *
 * @author weilai
 * @since 2026/01/15
 */
@Data
@Schema(description = "客户端用户信息响应")
public class ApiUserInfoResp {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 性别（0-未知 1-男 2-女）
     */
    @Schema(description = "性别（0-未知 1-男 2-女）", example = "1")
    private Integer gender;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "这是个人简介")
    private String description;

    /**
     * 积分
     */
    @Schema(description = "积分", example = "1000")
    private Integer points;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;
}
