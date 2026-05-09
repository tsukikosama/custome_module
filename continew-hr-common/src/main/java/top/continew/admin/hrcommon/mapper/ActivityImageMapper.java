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
import top.continew.admin.hrcommon.model.entity.ActivityImageDO;
import top.continew.admin.hrcommon.model.resp.ApiActivityImageResp;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 活动图片 Mapper
 *
 * @author weilai
 * @since 2026/05/06 17:52
 */
@Mapper
public interface ActivityImageMapper extends BaseMapper<ActivityImageDO> {

    /**
     * 自定义分页查询活动图片
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 活动图片列表
     */
    IPage<ApiActivityImageResp> customPage(@Param("page") Page page,
                                           @Param(Constants.WRAPPER) QueryWrapper<ActivityImageDO> wrapper);
}