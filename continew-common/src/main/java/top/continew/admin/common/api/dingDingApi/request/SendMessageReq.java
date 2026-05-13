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

package top.continew.admin.common.api.dingDingApi.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 钉钉发送消息请求
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "钉钉发送消息请求")
public class SendMessageReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 企业内部应用的AgentId
     */
    @Schema(description = "企业内部应用的AgentId", example = "4145772061")
    @JsonProperty("agent_id")
    private Long agentId = 4145772061L;

    /**
     * 接收消息的用户ID列表，多个用户用逗号分隔
     */
    @Schema(description = "接收消息的用户ID列表，多个用户用逗号分隔", example = "01511618041026628942")
    @JsonProperty("userid_list")
    private String useridList;

    /**
     * 接收的部门
     */
    @Schema(description = "接收消息的用户ID列表，多个用户用逗号分隔", example = "01511618041026628942")
    @JsonProperty("dept_id_list")
    private String deptIdList;

    /**
     * 是否发送给全体用户
     */
    @Schema(description = "是否发送给全体用户", example = "true")
    @JsonProperty("to_all_user")
    private Boolean toAllUser = false;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private Msg msg;

    /**
     * 是否开启ID转换
     */
    @Schema(description = "是否开启ID转换", example = "false")
    @JsonProperty("enable_id_trans")
    private Boolean enableIdTrans;

    /**
     * 消息内容
     */
    @Data
    @Schema(description = "消息内容")
    public static class Msg implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 消息类型
         */
        @Schema(description = "消息类型", example = "text")
        @JsonProperty("msgtype")
        private String msgType;

        /**
         * 文本消息内容
         */
        @Schema(description = "文本消息内容")
        private Text text;
    }

    /**
     * 文本消息内容
     */
    @Data
    @Schema(description = "文本消息内容")
    public static class Text implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 消息内容
         */
        @Schema(description = "消息内容", example = "oi")
        private String content;
    }
}
