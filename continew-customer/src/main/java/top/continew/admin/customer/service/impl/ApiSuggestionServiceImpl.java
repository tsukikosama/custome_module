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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.customer.model.req.SuggestionCreateReq;
import top.continew.admin.customer.model.req.SuggestionPageReq;
import top.continew.admin.customer.service.ApiSuggestionService;
import top.continew.admin.hrcommon.mapper.SuggestionMapper;
import top.continew.admin.hrcommon.model.entity.SuggestionDO;
import top.continew.admin.hrcommon.model.enums.SuggestionTagEnum;
import top.continew.admin.hrcommon.model.resp.ApiSuggestionResp;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * 客户端建议服务实现
 *
 * @author weilai
 * @since 2026/05/08
 */
@Service
@RequiredArgsConstructor
public class ApiSuggestionServiceImpl implements ApiSuggestionService {

    private final SuggestionMapper suggestionMapper;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void create(SuggestionCreateReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        SuggestionDO suggestionDO = new SuggestionDO();

        // 将标签值转换为枚举
        SuggestionTagEnum tagEnum = Arrays.stream(SuggestionTagEnum.values())
            .filter(e -> e.getValue().equals(req.getTag()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("无效的建议标签: " + req.getTag()));

        suggestionDO.setTag(tagEnum);
        suggestionDO.setContent(req.getContent());
        suggestionDO.setCreateUser(userId);
        suggestionDO.setCreateTime(LocalDateTime.now());

        suggestionMapper.insert(suggestionDO);
    }

    @Override
    public PageResp<ApiSuggestionResp> apiPage(SuggestionPageReq req) {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 创建分页对象
        Page<ApiSuggestionResp> page = new Page<>(req.getPage(), req.getSize());

        // 构建 WHERE 条件
        QueryWrapper wrapper = new QueryWrapper();

        // 执行分页查询
        IPage<ApiSuggestionResp> result = suggestionMapper.customerPage(page, wrapper);

        return PageResp.build(result);
    }

}
