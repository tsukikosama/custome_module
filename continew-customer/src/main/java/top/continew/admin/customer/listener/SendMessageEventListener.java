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

package top.continew.admin.customer.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.common.api.dingDingApi.request.SendMessageReq;
import top.continew.admin.customer.event.SendMessageEvent;
import top.continew.admin.hrcommon.model.entity.user.UserDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 发送消息事件监听器
 *
 * @author weilai
 * @since 2026/05/13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SendMessageEventListener {

    private final DingTalkApiService dingTalkApiService;

    /**
     * 监听发送消息事件，发送钉钉推送
     * 使用 @TransactionalEventListener 确保在主事务提交后再执行
     * 使用 @Async 实现异步执行，不阻塞主流程
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendMessage(SendMessageEvent event) {
        try {

            // 1. 添加发送人列表
            List<UserDO> senders = event.getSenders();

            if (senders.isEmpty()) {
                log.warn("发送消息推送失败：没有需要通知的用户");
                return;
            }

            // 获取消息内容
            String content = event.getContent();

            // 构建钉钉用户ID列表（逗号分隔）
            String useridList = senders.stream()
                .map(UserDO::getDingdingId)
                .filter(dingdingId -> dingdingId != null)
                .collect(Collectors.joining(","));

            if (useridList.isEmpty()) {
                log.warn("发送消息推送失败：所有用户的钉钉ID都为空");
                return;
            }

            // 构建发送消息请求
            SendMessageReq sendMessageReq = new SendMessageReq();
            sendMessageReq.setUseridList(useridList);

            // 构建消息内容
            SendMessageReq.Msg msg = new SendMessageReq.Msg();
            msg.setMsgType("text");

            SendMessageReq.Text text = new SendMessageReq.Text();
            text.setContent(content);
            msg.setText(text);

            sendMessageReq.setMsg(msg);

            // 发送钉钉会话消息
            dingTalkApiService.sendConversationMessage(sendMessageReq,event.getFlag());
            log.info("发送消息钉钉推送成功：通知用户数={}，用户列表={}", senders.size(), senders.stream()
                .map(UserDO::getNickname)
                .collect(Collectors.joining(", ")));

        } catch (Exception e) {
            // 推送失败不影响主流程，仅记录日志
            log.error("发送消息钉钉推送失败：错误={}", e.getMessage(), e);
        }
    }
}
