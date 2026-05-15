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

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.hrcommon.mapper.ProductTypeMapper;
import top.continew.admin.hrcommon.model.entity.ProductTypeDO;
import top.continew.admin.hrcommon.model.resp.ProductTypeResp;
import top.continew.admin.customer.service.ProductTypeService;

import java.util.List;

/**
 * 商品分类服务实现
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductTypeResp> list() {
        List<ProductTypeDO> productTypeList = productTypeMapper.selectList(null);
        return BeanUtil.copyToList(productTypeList, ProductTypeResp.class);
    }
}
