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
 * 客户端消息详情响应
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Data
@Schema(description = "客户端消息详情响应")
public class ApiMessageDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @Schema(description = "消息ID", example = "1")
    private Long id;

    /**
     * 消息类型（1系统消息/2安全消息）
     */
    @Schema(description = "消息类型（1系统消息/2安全消息）", example = "1")
    private Integer type;

    /**
     * 消息类型文本描述
     */
    @Schema(description = "消息类型文本描述", example = "系统消息")
    private String typeText;

    /**
     * 消息标题
     */
    @Schema(description = "消息标题", example = "系统维护通知")
    private String title;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容", example = "尊敬的各位同事：系统将于2026年5月1日00:00至06:00进行升级维护...")
    private String content;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径", example = "/pages/activity/detail/1")
    private String path;

    /**
     * 创建时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "创建时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-04-29 10:00:00")
    private String createTime;

    /**
     * 是否已读
     */
    @Schema(description = "是否已读", example = "false")
    private Boolean isRead;
}
