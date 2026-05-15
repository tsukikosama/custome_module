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

package top.continew.admin.customer.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import top.continew.admin.hrcommon.model.entity.user.UserDO;

import java.util.List;

/**
 * 发送消息通知事件
 *
 * @author weilai
 * @since 2026/05/13
 */
@Getter
public class SendMessageEvent extends ApplicationEvent {

    /**
     * 发送人列表（触发消息的用户）
     */
    private final List<UserDO> senders;

    /**
     * 消息内容
     */
    private final String content;

    /**
     * 额外需要通知的用户列表（可选）
     */
    private final List<UserDO> additionalUsers;

    private final Boolean flag;

    public SendMessageEvent(Object source, List<UserDO> senders, String content, Boolean flag) {
        super(source);
        this.senders = senders;
        this.content = content;
        this.additionalUsers = null;
        this.flag = flag;
    }

    public SendMessageEvent(Object source,
                            List<UserDO> senders,
                            String content,
                            List<UserDO> additionalUsers,
                            Boolean flag) {
        super(source);
        this.senders = senders;
        this.content = content;
        this.additionalUsers = additionalUsers;
        this.flag = flag;
    }
}
