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
import top.continew.admin.hrcommon.model.entity.ActivityDO;
import top.continew.admin.hrcommon.model.resp.ActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ActivityResp;
import top.continew.admin.hrcommon.model.resp.ApiActivityDetailResp;
import top.continew.admin.hrcommon.model.resp.ApiActivityResp;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 活动 Mapper
 *
 * @author weilai
 * @since 2026/05/06 10:48
 */
@Mapper
public interface ActivityMapper extends BaseMapper<ActivityDO> {

    /**
     * 自定义分页查询活动信息（包含演讲人、必须参加人、主动参加人列表）
     *
     * @return 活动信息列表
     */
    IPage<ActivityResp> customPage(@Param("page") Page page,
                                   @Param(Constants.WRAPPER) QueryWrapper<ActivityDO> wrapper);

    /**
     * 根据ID查询活动详情（包含演讲人、必须参加人、主动参加人列表）
     *
     * @param id 活动ID
     * @return 活动详情
     */
    ActivityDetailResp getDetailById(@Param("id") Long id);

    /**
     * 客户端分页查询活动（只查询已审核通过的活动）
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 活动列表
     */
    IPage<ApiActivityResp> apiPage(@Param("page") Page page,
                                   @Param(Constants.WRAPPER) QueryWrapper<ActivityDO> wrapper);

    /**
     * 查询活动详情（包含成员信息，用于客户端详情）
     *
     * @param activityId 活动ID
     * @return 活动详情对象
     */
    ApiActivityDetailResp getActivityMemberInfo(@Param("activityId") Long activityId);

}