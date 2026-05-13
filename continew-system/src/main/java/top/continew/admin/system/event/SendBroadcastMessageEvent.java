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

package top.continew.admin.system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 发送广播消息通知事件
 *
 * @author weilai
 * @since 2026/05/13
 */
@Getter
public class SendBroadcastMessageEvent extends ApplicationEvent {

    /**
     * 消息内容
     */
    private final String content;

    private final boolean flag;

    public SendBroadcastMessageEvent(Object source, String content, boolean flag) {
        super(source);
        this.content = content;
        this.flag = flag;
    }
}
