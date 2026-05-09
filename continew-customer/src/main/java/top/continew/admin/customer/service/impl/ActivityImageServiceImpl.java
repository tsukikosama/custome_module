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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.hrcommon.mapper.ActivityImageMapper;
import top.continew.admin.hrcommon.model.entity.ActivityImageDO;
import top.continew.admin.hrcommon.model.resp.ApiActivityImageResp;
import top.continew.admin.customer.model.req.ActivityImageCreateReq;
import top.continew.admin.customer.service.ActivityImageService;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 活动图片服务实现
 *
 * @author weilai
 * @since 2026/05/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityImageServiceImpl implements ActivityImageService {

    private final ActivityImageMapper activityImageMapper;

    @Override
    public PageResp<ApiActivityImageResp> page(Long activityId, Integer page, Integer size) {
        // 创建分页对象
        Page<ActivityImageDO> pageObj = new Page<>(page, size);

        // 构建查询条件
        QueryWrapper<ActivityImageDO> wrapper = new QueryWrapper<>();
        wrapper.eq("bai.activity_id", activityId);

        // 执行自定义分页查询
        IPage<ApiActivityImageResp> result = activityImageMapper.customPage(pageObj, wrapper);

        // 构建分页响应
        return PageResp.build(result);
    }

    @Override
    public void save(ActivityImageCreateReq req) {
        // 创建活动图片实体
        ActivityImageDO activityImage = new ActivityImageDO();
        activityImage.setActivityId(req.getActivityId());
        activityImage.setImgUrl(req.getImgUrl());

        // 保存到数据库
        activityImageMapper.insert(activityImage);
    }
}
