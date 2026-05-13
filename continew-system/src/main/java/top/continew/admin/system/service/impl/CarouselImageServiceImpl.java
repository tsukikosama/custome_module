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

package top.continew.admin.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.hrcommon.mapper.CarouselImageMapper;
import top.continew.admin.hrcommon.mapper.resp.CarouselImageDetailResp;
import top.continew.admin.hrcommon.mapper.resp.CarouselImageResp;
import top.continew.admin.hrcommon.model.entity.CarouselImageDO;
import top.continew.admin.system.model.query.CarouselImageQuery;
import top.continew.admin.system.model.req.CarouselImageReq;
import top.continew.admin.system.service.CarouselImageService;
import top.continew.starter.core.util.validation.CheckUtils;

/**
 * 轮播图业务实现
 *
 * @author weilai
 * @since 2026/05/09 09:37
 */
@Service
@RequiredArgsConstructor
public class CarouselImageServiceImpl extends BaseServiceImpl<CarouselImageMapper, CarouselImageDO, CarouselImageResp, CarouselImageDetailResp, CarouselImageQuery, CarouselImageReq> implements CarouselImageService {
    @Override
    protected void beforeCreate(CarouselImageReq req) {
        //校验当前标题是否已存在
        Long l = this.baseMapper.selectCount(Wrappers.<CarouselImageDO>lambdaQuery()
            .eq(CarouselImageDO::getTitle, req.getTitle()));
        CheckUtils.throwIf(l > 0, "标题已存在");
        super.beforeCreate(req);
    }
}