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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.customer.service.CarouselService;
import top.continew.admin.hrcommon.mapper.CarouselImageMapper;
import top.continew.admin.hrcommon.model.entity.CarouselImageDO;
import top.continew.admin.hrcommon.model.resp.ApiCarouselResp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端轮播图服务实现
 *
 * @author weilai
 * @since 2026/05/09
 */
@Service
@RequiredArgsConstructor
public class CarouselServiceImpl implements CarouselService {

    private final CarouselImageMapper carouselImageMapper;

    @Override
    public List<ApiCarouselResp> list() {
        // 构建查询条件（列表不查询 content 字段，避免大字段导致 PacketTooBig 异常）
        QueryWrapper<CarouselImageDO> wrapper = new QueryWrapper<>();
        wrapper.select("id", "url", "sort", "title", "jump_path");
        // 按 sort 字段升序排序（数字小的在前）
        wrapper.orderByAsc("sort");

        // 查询所有轮播图
        List<CarouselImageDO> carouselImages = carouselImageMapper.selectList(wrapper);

        // 转换为客户端API响应格式
        return carouselImages.stream().map(this::convertToApiResp).collect(Collectors.toList());
    }

    @Override
    public ApiCarouselResp getById(Long id) {
        CarouselImageDO carouselImage = carouselImageMapper.selectById(id);
        if (carouselImage == null) {
            return null;
        }
        return convertToApiResp(carouselImage);
    }

    /**
     * 转换为客户端API响应格式
     *
     * @param carouselImage 轮播图实体
     * @return 客户端API响应
     */
    private ApiCarouselResp convertToApiResp(CarouselImageDO carouselImage) {
        ApiCarouselResp apiResp = new ApiCarouselResp();
        apiResp.setId(carouselImage.getId());
        apiResp.setUrl(carouselImage.getUrl());
        apiResp.setSort(carouselImage.getSort());
        apiResp.setTitle(carouselImage.getTitle());
        apiResp.setJumpPath(carouselImage.getJumpPath());
        apiResp.setContent(carouselImage.getContent());
        return apiResp;
    }
}
