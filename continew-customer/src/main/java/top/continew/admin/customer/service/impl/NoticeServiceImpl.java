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

package top.continew.admin.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.customer.model.req.NoticePageReq;
import top.continew.admin.customer.service.NoticeService;
import top.continew.admin.common.mapper.notice.NoticeLogMapper;
import top.continew.admin.common.mapper.notice.NoticeMapper;
import top.continew.admin.common.model.entity.notice.NoticeDO;
import top.continew.admin.common.model.entity.notice.NoticeLogDO;
import top.continew.admin.common.model.query.NoticeQuery;
import top.continew.admin.common.model.resp.notice.NoticeDetailResp;
import top.continew.admin.common.model.resp.notice.NoticeResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;

/**
 * 客户端公告服务实现
 *
 * @author weilai
 * @since 2026/05/09
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final NoticeLogMapper noticeLogMapper;

    @Override
    public PageResp<NoticeResp> page(NoticePageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 构建查询条件
        NoticeQuery query = new NoticeQuery();
        query.setUserId(userId);
        query.setType(req.getType());
        // 创建分页对象并执行查询
        Page<NoticeDO> page = new Page<>(req.getPage(), req.getSize());
        IPage<NoticeResp> result = noticeMapper.selectNoticePage(page, query);

        return PageResp.build(result);
    }

    @Override
    public NoticeDetailResp get(Long id) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询公告详情
        NoticeDetailResp noticeDetail = noticeMapper.selectNoticeById(id);
        if (noticeDetail == null) {
            return null;
        }

        // 检查公告状态（只显示已发布的）
        if (noticeDetail.getStatus() == null || noticeDetail.getStatus().getValue() != 3) {
            return null;
        }

        // 标记为已读
        readNotice(id);

        return noticeDetail;
    }

    @Override
    public void readNotice(Long id) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 检查是否已读过
        NoticeLogDO existingLog = noticeLogMapper
            .selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<NoticeLogDO>()
                .eq(NoticeLogDO::getNoticeId, id)
                .eq(NoticeLogDO::getUserId, userId));

        // 如果未读过，则插入阅读记录
        if (existingLog == null) {
            NoticeLogDO noticeLog = new NoticeLogDO(id, userId, LocalDateTime.now());
            noticeLogMapper.insert(noticeLog);
        }
    }

    @Override
    public Integer getUnreadCount() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询未读公告ID列表（noticeMethod 为 null 表示查询所有通知方式）
        java.util.List<Long> unreadIds = noticeMapper.selectUnreadIdsByUserId(null, userId);

        // 返回未读数量
        return unreadIds.size();
    }
}
