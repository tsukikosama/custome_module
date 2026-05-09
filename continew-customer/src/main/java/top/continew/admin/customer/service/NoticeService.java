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

import top.continew.admin.hrcommon.model.resp.notice.NoticeDetailResp;
import top.continew.admin.hrcommon.model.resp.notice.NoticeResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 客户端公告服务
 *
 * @author weilai
 * @since 2026/05/09
 */
public interface NoticeService {

    /**
     * 分页查询当前用户的公告列表
     *
     * @return 公告分页结果
     */
    PageResp<NoticeResp> page();

    /**
     * 查询公告详情
     *
     * @param id 公告ID
     * @return 公告详情
     */
    NoticeDetailResp get(Long id);

    /**
     * 标记公告为已读
     *
     * @param id 公告ID
     */
    void readNotice(Long id);
}
