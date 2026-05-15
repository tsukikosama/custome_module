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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.continew.admin.controller.biz.model.entity.ActivityMemberDO;
import top.continew.admin.hrcommon.model.resp.ActivityMemberCustomResp;
import top.continew.admin.hrcommon.model.resp.UserActivityStatResp;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 活动参与用户 Mapper
 *
 * @author weilai
 * @since 2026/05/06 11:03
 */
@Mapper
public interface ActivityMemberMapper extends BaseMapper<ActivityMemberDO> {

    /**
     * 自定义分页查询活动参与用户
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 活动参与用户自定义分页响应
     */
    IPage<ActivityMemberCustomResp> customPage(Page<Object> page, @Param("ew") QueryWrapper<ActivityMemberDO> wrapper);

    /**
     * 批量插入活动参与用户
     *
     * @param activityMembers 活动参与用户列表
     * @return 插入的记录数
     */

    /**
     * 统计用户参与的活动
     *
     * @param userId 用户ID
     * @return 用户活动统计响应
     */
    UserActivityStatResp statUserActivities(@Param("userId") Long userId);

}