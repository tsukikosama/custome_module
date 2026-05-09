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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.service.NoticeService;
import top.continew.admin.hrcommon.model.resp.notice.NoticeDetailResp;
import top.continew.admin.hrcommon.model.resp.notice.NoticeResp;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.annotation.Log;

/**
 * 公告 API
 *
 * @author weilai
 * @since 2026/05/09
 */
@Tag(name = "公告 API")
@Log(module = "公告管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
public class ApiNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页查询公告列表", description = "分页查询当前登录用户的公告列表")
    @GetMapping
    public PageResp<NoticeResp> page() {
        return noticeService.page();
    }

    @Operation(summary = "查询公告详情", description = "查询单个公告的详细信息")
    @GetMapping("/{id}")
    public NoticeDetailResp get(@Parameter(description = "公告ID", required = true, example = "1") @PathVariable Long id) {
        return noticeService.get(id);
    }

    @Operation(summary = "标记公告为已读", description = "将指定的公告标记为已读状态")
    @PostMapping("/{id}/read")
    public void read(@Parameter(description = "公告ID", required = true, example = "1") @PathVariable Long id) {
        noticeService.readNotice(id);
    }
}
