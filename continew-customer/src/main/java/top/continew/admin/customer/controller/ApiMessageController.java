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

package top.continew.admin.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.model.req.MessagePageReq;
import top.continew.admin.customer.model.req.ReadMessageReq;
import top.continew.admin.customer.model.resp.ApiMessageResp;
import top.continew.admin.customer.model.resp.UnreadCountResp;
import top.continew.admin.customer.service.MessageService;
import top.continew.admin.hrcommon.model.resp.message.MessageDetailResp;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 消息 API
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Tag(name = "消息 API")
@Log(module = "消息管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class ApiMessageController {

    private final MessageService messageService;


    @Operation(summary = "分页查询消息列表", description = "分页查询当前登录用户的消息列表")
    @GetMapping
    public PageResp<ApiMessageResp> page(@Valid MessagePageReq req) {
        return messageService.page(req);
    }

    @Operation(summary = "查询消息详情", description = "查询单个消息的详细信息")
    @GetMapping("/{id}")
    public MessageDetailResp get(@Parameter(description = "消息ID", required = true, example = "1") @PathVariable Long id) {
        return messageService.get(id);
    }

    @Log(ignore = true)
    @Operation(summary = "查询未读消息数量", description = "查询当前登录用户的未读消息数量")
    @GetMapping("/unreadCount")
    public UnreadCountResp unreadCount() {
        return messageService.getUnreadCount();
    }

    @Operation(summary = "标记消息为已读", description = "将指定的消息标记为已读状态（支持批量）")
    @PatchMapping("/read")
    public void read(@RequestBody @Valid ReadMessageReq req) {
        messageService.readMessage(req);
    }
}
