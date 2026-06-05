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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.common.enums.DisEnableStatusEnum;
import top.continew.admin.common.mapper.dept.DeptMapper;
import top.continew.admin.common.mapper.user.UserMapper;
import top.continew.admin.customer.model.resp.ApiDeptDetailResp;
import top.continew.admin.customer.model.resp.ApiDeptResp;
import top.continew.admin.customer.service.DeptService;
import top.continew.admin.common.model.entity.dept.DeptDO;
import top.continew.admin.common.model.entity.user.UserDO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端部门服务实现
 *
 * @author weilai
 * @since 2026/05/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;
    private final UserMapper userMapper;

    @Override
    public List<ApiDeptResp> list() {
        // 查询 parent_id=1 的所有启用部门，按排序字段升序排列
        List<DeptDO> deptList = deptMapper.selectList(Wrappers.<DeptDO>lambdaQuery()
            .eq(DeptDO::getParentId, 1L)
            .eq(DeptDO::getStatus, DisEnableStatusEnum.ENABLE)
            .orderByAsc(DeptDO::getSort));

        // 转换为响应对象
        return deptList.stream().map(this::convertToApiResp).collect(Collectors.toList());
    }

    @Override
    public List<ApiDeptDetailResp> getDetail(Long deptId) {
        List<ApiDeptDetailResp> result = new ArrayList<>();

        // 查询该部门的直接成员
        List<UserDO> directMembers = userMapper.selectList(Wrappers.<UserDO>lambdaQuery()
            .eq(UserDO::getDeptId, deptId)
            .eq(UserDO::getStatus, DisEnableStatusEnum.ENABLE));

        // 如果该部门有直接成员，添加到结果列表
        if (!directMembers.isEmpty()) {
            DeptDO dept = deptMapper.selectById(deptId);
            ApiDeptDetailResp directResp = new ApiDeptDetailResp();
            directResp.setId(dept.getId());
            directResp.setName(dept.getName());
            directResp.setDescription(dept.getDescription());
            directResp.setMembers(directMembers.stream()
                .map(this::convertToMemberResp)
                .collect(Collectors.toList()));
            result.add(directResp);
        }

        // 查询所有子部门，按排序字段升序排列
        List<DeptDO> childDeptList = deptMapper.selectList(Wrappers.<DeptDO>lambdaQuery()
            .eq(DeptDO::getParentId, deptId)
            .eq(DeptDO::getStatus, DisEnableStatusEnum.ENABLE)
            .orderByAsc(DeptDO::getSort));

        // 为每个子部门查询其成员并添加到结果列表
        for (DeptDO childDept : childDeptList) {
            result.add(convertToDetailResp(childDept));
        }

        return result;
    }

    /**
     * 转换为部门列表响应对象
     *
     * @param dept 部门实体
     * @return 部门响应对象
     */
    private ApiDeptResp convertToApiResp(DeptDO dept) {
        ApiDeptResp apiResp = new ApiDeptResp();
        apiResp.setId(dept.getId());
        apiResp.setName(dept.getName());
        apiResp.setDescription(dept.getDescription());
        return apiResp;
    }

    /**
     * 转换为部门详情响应对象（包含成员列表）
     *
     * @param dept 部门实体
     * @return 部门详情响应对象
     */
    private ApiDeptDetailResp convertToDetailResp(DeptDO dept) {
        // 查询该部门的启用成员
        List<UserDO> members = userMapper.selectList(Wrappers.<UserDO>lambdaQuery()
            .eq(UserDO::getDeptId, dept.getId())
            .eq(UserDO::getStatus, DisEnableStatusEnum.ENABLE));

        // 转换为详情响应对象
        ApiDeptDetailResp detailResp = new ApiDeptDetailResp();
        detailResp.setId(dept.getId());
        detailResp.setName(dept.getName());
        // 转换成员列表
        List<ApiDeptDetailResp.DeptMemberResp> memberResps = members.stream()
            .map(this::convertToMemberResp)
            .collect(Collectors.toList());

        detailResp.setMembers(memberResps);

        return detailResp;
    }

    /**
     * 转换为部门成员响应对象
     *
     * @param user 用户实体
     * @return 部门成员响应对象
     */
    private ApiDeptDetailResp.DeptMemberResp convertToMemberResp(UserDO user) {
        ApiDeptDetailResp.DeptMemberResp memberResp = new ApiDeptDetailResp.DeptMemberResp();
        memberResp.setUserId(user.getId());
        memberResp.setNickname(user.getNickname());
        memberResp.setAvatar(user.getAvatar());
        memberResp.setPhone(user.getPhone());
        memberResp.setDescription(user.getDescription());
        memberResp.setJobTitle(user.getJobTitle());
        return memberResp;
    }
}
