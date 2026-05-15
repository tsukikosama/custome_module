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

import java.io.Serializable;
import java.util.List;

public class TaskCreateReq implements Serializable {

    /**
     * 主题
     */
    private String subject;

    /**
     * 描述
     */
    private String description;

    /**
     * 截止时间（时间戳）
     */
    private Long dueTime;

    /**
     * 执行人 ID 列表
     */
    private List<String> executorIds;

    /**
     * 参与人 ID 列表
     */
    private List<String> participantIds;

    /**
     * 通知配置
     */
    private NotifyConfigs notifyConfigs;

    // getter / setter
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDueTime() {
        return dueTime;
    }

    public void setDueTime(Long dueTime) {
        this.dueTime = dueTime;
    }

    public List<String> getExecutorIds() {
        return executorIds;
    }

    public void setExecutorIds(List<String> executorIds) {
        this.executorIds = executorIds;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public NotifyConfigs getNotifyConfigs() {
        return notifyConfigs;
    }

    public void setNotifyConfigs(NotifyConfigs notifyConfigs) {
        this.notifyConfigs = notifyConfigs;
    }

    public static class NotifyConfigs implements Serializable {

        /**
         * 钉钉通知配置
         */
        private String dingNotify;

        public String getDingNotify() {
            return dingNotify;
        }

        public void setDingNotify(String dingNotify) {
            this.dingNotify = dingNotify;
        }
    }
}
