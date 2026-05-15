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

package top.continew.admin.customer.service;

import top.continew.admin.customer.model.req.MessagePageReq;
import top.continew.admin.customer.model.req.ReadMessageReq;
import top.continew.admin.customer.model.resp.ApiMessageResp;
import top.continew.admin.customer.model.resp.UnreadCountResp;
import top.continew.admin.hrcommon.model.resp.message.MessageDetailResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 客户端消息服务
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
public interface MessageService {

    /**
     * 分页查询当前用户的消息列表
     *
     * @param req 查询请求
     * @return 消息分页结果
     */
    PageResp<ApiMessageResp> page(MessagePageReq req);

    /**
     * 查询消息详情
     *
     * @param id 消息ID
     * @return 消息详情
     */
    MessageDetailResp get(Long id);

    /**
     * 获取未读消息数量
     *
     * @return 未读消息数量
     */
    UnreadCountResp getUnreadCount();

    /**
     * 标记消息为已读
     *
     * @param req 请求参数（包含消息ID列表，为空则将所有消息标记为已读）
     */
    void readMessage(ReadMessageReq req);
}
