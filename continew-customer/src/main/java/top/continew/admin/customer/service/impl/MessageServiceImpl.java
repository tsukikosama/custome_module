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
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.customer.model.req.MessagePageReq;
import top.continew.admin.customer.model.req.ReadMessageReq;
import top.continew.admin.customer.model.resp.ApiMessageResp;
import top.continew.admin.customer.model.resp.UnreadCountResp;
import top.continew.admin.customer.service.MessageService;
import top.continew.admin.common.mapper.message.MessageLogMapper;
import top.continew.admin.common.mapper.message.MessageMapper;
import top.continew.admin.common.model.entity.message.MessageDO;
import top.continew.admin.common.model.entity.message.MessageLogDO;
import top.continew.admin.hrcommon.model.query.MessageQuery;
import top.continew.admin.hrcommon.model.resp.message.MessageDetailResp;
import top.continew.admin.hrcommon.model.resp.message.MessageResp;
import top.continew.starter.core.util.CollUtils;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端消息服务实现
 *
 * @author weilai
 * @since 2026/01/19 14:50
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final MessageLogMapper messageLogMapper;

    @Override
    public PageResp<ApiMessageResp> page(MessagePageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 构建查询条件
        MessageQuery query = new MessageQuery();
        query.setUserId(userId);
        if (req.getType() != null) {
            query.setType(req.getType());
        }

        // 创建分页对象并执行查询（使用MessageDO类型的Page对象）
        Page<top.continew.admin.hrcommon.model.entity.MessageDO> page = new Page<>(req.getPage(), req.getSize());
        IPage<MessageResp> result = messageMapper.selectMessagePage(page, query);

        // 转换为客户端API响应格式
        List<ApiMessageResp> apiRecords = result.getRecords()
            .stream()
            .map(this::convertToApiResp)
            .collect(Collectors.toList());

        // 构建客户端API分页结果
        Page<ApiMessageResp> apiPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        apiPage.setRecords(apiRecords);

        return PageResp.build(apiPage);
    }

    @Override
    public MessageDetailResp get(Long id) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 使用带权限过滤的查询方法（与管理后台一致的查询）
        return messageMapper.selectMessageByIdForUser(id, userId);
    }

    @Override
    public UnreadCountResp getUnreadCount() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询未读消息数量（type为null表示查询所有类型）
        Long count = messageMapper.selectUnreadCountByUserIdAndType(userId, null);

        return UnreadCountResp.builder().count(count).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readMessage(ReadMessageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询当前用户的未读消息
        List<MessageDO> list = messageMapper.selectUnreadListByUserId(userId);
        List<Long> unreadIds = CollUtils.mapToList(list, MessageDO::getId);

        // 确定需要标记为已读的消息ID列表
        List<Long> idsToMark = CollUtil.isNotEmpty(req.getIds())
            ? CollUtil.intersection(unreadIds, req.getIds()).stream().toList()
            : unreadIds;

        // 批量插入消息已读记录
        if (CollUtil.isNotEmpty(idsToMark)) {
            LocalDateTime now = LocalDateTime.now();
            for (Long messageId : idsToMark) {
                MessageLogDO messageLog = new MessageLogDO(messageId, userId, now);
                messageLogMapper.insert(messageLog);
            }
        }
    }

    /**
     * 转换为客户端API响应格式
     *
     * @param messageResp 消息响应对象
     * @return 客户端API响应
     */
    private ApiMessageResp convertToApiResp(MessageResp messageResp) {
        ApiMessageResp apiResp = new ApiMessageResp();
        apiResp.setId(messageResp.getId());

        // 消息类型转换
        if (messageResp.getType() != null) {
            apiResp.setType(messageResp.getType().getValue());
            apiResp.setTypeText(messageResp.getType().getDescription());
        }

        apiResp.setTitle(messageResp.getTitle());
        apiResp.setPath(messageResp.getPath());
        apiResp.setCreateTime(messageResp.getCreateTime() != null ? messageResp.getCreateTime().toString() : null);

        // 设置已读状态（从SQL查询结果中获取）
        apiResp.setIsRead(messageResp.getIsRead() != null && messageResp.getIsRead());

        return apiResp;
    }

}
