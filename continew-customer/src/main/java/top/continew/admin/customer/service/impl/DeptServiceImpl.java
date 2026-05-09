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
import top.continew.admin.hrcommon.mapper.dept.DeptMapper;
import top.continew.admin.hrcommon.mapper.user.UserMapper;
import top.continew.admin.customer.model.resp.ApiDeptDetailResp;
import top.continew.admin.customer.model.resp.ApiDeptResp;
import top.continew.admin.customer.service.DeptService;
import top.continew.admin.hrcommon.model.entity.dept.DeptDO;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.starter.core.exception.BusinessException;

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
        // 查询所有启用的部门，按排序字段升序排列
        List<DeptDO> deptList = deptMapper.selectList(Wrappers.<DeptDO>lambdaQuery()
            .eq(DeptDO::getStatus, DisEnableStatusEnum.ENABLE)
            .orderByAsc(DeptDO::getSort));

        // 转换为响应对象
        return deptList.stream().map(this::convertToApiResp).collect(Collectors.toList());
    }

    @Override
    public ApiDeptDetailResp getDetail(Long deptId) {
        // 查询部门信息
        DeptDO dept = deptMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException("部门不存在或已被删除");
        }

        // 只允许查询启用的部门
        if (!DisEnableStatusEnum.ENABLE.equals(dept.getStatus())) {
            throw new BusinessException("该部门已被禁用");
        }

        // 查询该部门的启用成员
        List<UserDO> members = userMapper.selectList(Wrappers.<UserDO>lambdaQuery()
            .eq(UserDO::getDeptId, deptId)
            .eq(UserDO::getStatus, DisEnableStatusEnum.ENABLE));

        // 转换为详情响应对象
        ApiDeptDetailResp detailResp = new ApiDeptDetailResp();
        detailResp.setId(dept.getId());
        detailResp.setName(dept.getName());
        detailResp.setDescription(dept.getDescription());

        // 转换成员列表
        List<ApiDeptDetailResp.DeptMemberResp> memberResps = members.stream()
            .map(this::convertToMemberResp)
            .collect(Collectors.toList());

        detailResp.setMembers(memberResps);

        return detailResp;
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
        return memberResp;
    }
}
