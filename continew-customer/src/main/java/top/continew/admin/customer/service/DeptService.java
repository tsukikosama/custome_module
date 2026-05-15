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

package top.continew.admin.customer.service;

import top.continew.admin.customer.model.resp.ApiDeptDetailResp;
import top.continew.admin.customer.model.resp.ApiDeptResp;

import java.util.List;

/**
 * 客户端部门服务
 *
 * @author weilai
 * @since 2026/05/08
 */
public interface DeptService {

    /**
     * 查询所有启用的部门
     *
     * @return 部门列表
     */
    List<ApiDeptResp> list();

    /**
     * 查询指定部门的子部门列表
     *
     * @param deptId 父部门ID
     * @return 子部门详情列表
     */
    List<ApiDeptDetailResp> getDetail(Long deptId);
}
