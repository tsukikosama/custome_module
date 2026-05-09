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

package top.continew.admin.hrcommon.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.continew.admin.hrcommon.model.entity.WishDO;
import top.continew.admin.hrcommon.model.resp.WishResp;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 心愿表 Mapper
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Mapper
public interface WishMapper extends BaseMapper<WishDO> {

    /**
     * 自定义分页查询（只查询父心愿，并统计子心愿数量）
     *
     * @param page    分页参数
     * @param wrapper 查询条件
     * @return 分页结果
     */
    IPage<WishResp> customParentPage(Page<WishResp> page, @Param(Constants.WRAPPER) QueryWrapper<WishDO> wrapper);
}