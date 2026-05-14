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

package top.continew.admin.system.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.api.dingDingApi.request.SendMessageReq;
import top.continew.admin.system.event.SendBroadcastMessageEvent;

/**
 * 发送广播消息事件监听器
 *
 * @author weilai
 * @since 2026/05/13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SendBroadcastMessageEventListener {

    private final DingTalkApiService dingTalkApiService;

    /**
     * 监听发送广播消息事件，发送钉钉推送（全体用户）
     * 使用 @TransactionalEventListener 确保在主事务提交后再执行
     * 使用 @Async 实现异步执行，不阻塞主流程
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendBroadcastMessage(SendBroadcastMessageEvent event) {
        try {

            // 获取消息内容
            String content = event.getContent();

            // 构建发送消息请求
            SendMessageReq sendMessageReq = new SendMessageReq();
            sendMessageReq.setToAllUser(true);

            // 构建消息内容
            SendMessageReq.Msg msg = new SendMessageReq.Msg();
            msg.setMsgType("text");

            SendMessageReq.Text text = new SendMessageReq.Text();
            text.setContent(content);
            msg.setText(text);

            sendMessageReq.setMsg(msg);
            sendMessageReq.setToAllUser(true);
            // 发送钉钉会话消息（全体用户）
            dingTalkApiService.sendConversationMessage(sendMessageReq,event.isFlag());
            log.info("发送广播消息钉钉推送成功：内容={}", content);

        } catch (Exception e) {
            // 推送失败不影响主流程，仅记录日志
            log.error("发送广播消息钉钉推送失败：错误={}", e.getMessage(), e);
        }
    }
}
