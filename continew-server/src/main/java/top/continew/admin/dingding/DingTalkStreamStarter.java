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

package top.continew.admin.dingding;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shade.com.alibaba.fastjson2.JSONObject;
import top.continew.admin.hrcommon.model.entity.StreamEventDO;
import top.continew.admin.system.service.StreamEventService;

@Component
@Slf4j
@RequiredArgsConstructor
public class DingTalkStreamStarter implements InitializingBean {

    @Value("${dingtalk.clientId}")
    private String clientId;

    @Value("${dingtalk.clientSecret}")
    private String clientSecret;

    private final StreamEventService streamEventService;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("钉钉stream开始了");
        OpenDingTalkStreamClientBuilder.custom()
            .credential(new AuthClientCredential(clientId, clientSecret))
            .registerAllEventListener(new GenericEventListener() {
                @Override
                public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                    try {
                        String eventId = event.getEventId();
                        String eventType = event.getEventType();
                        Long bornTime = event.getEventBornTime();

                        JSONObject bizData = event.getData();

                        log.info("收到钉钉事件: id={}, type={}, time={}, data={}", eventId, eventType, bornTime, bizData);

                        process(eventId, eventType, bornTime, bizData);
                        return EventAckStatus.SUCCESS;
                    } catch (Exception e) {
                        log.error("处理钉钉事件失败", e);
                        return EventAckStatus.LATER;
                    }
                }
            })
            .build()
            .start();
    }

    private void process(String eventId, String evenType, Long bornTime, Object eventObj) {
        JSONObject event = (JSONObject)eventObj;
        //bpms_instance_change 判断type是不是这个类型
        if (evenType.equals("bpms_instance_change")) {
            // 获取 title 看看是否包含加班

            String title = event.getString("title");
            if (StrUtil.isBlank(title) || !title.contains("加班")) {
                return;
            }
            StreamEventDO streamEventDO = new StreamEventDO();
            streamEventDO.setEventId(eventId);
            streamEventDO.setType(evenType);
            streamEventDO.setTime(bornTime);
            String processInstanceId = event.getString("processInstanceId");
            streamEventDO.setProcessInstanceId(processInstanceId);
            streamEventDO.setContent(event.toJSONString());
            StreamEventDO one = streamEventService.getOne(Wrappers.<StreamEventDO>lambdaQuery()
                .eq(StreamEventDO::getProcessInstanceId, processInstanceId));
            if (one != null) {
                return;
            }
            // 如果是 就把这条通知记录保存下来
            streamEventService.save(streamEventDO);
        }

    }

}
