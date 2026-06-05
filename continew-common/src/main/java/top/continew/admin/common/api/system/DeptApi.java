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

package top.continew.admin.common.api.system;

import top.continew.admin.common.model.resp.DeptResp;

/**
 * 部门 API 接口
 * <p>
 * 用于解耦 hr-common 与 system 模块的依赖关系
 * crane4j 的 @AssembleMethod 注解需要引用这个接口
 *
 * @author Charles7c
 * @since 2026/05/07
 */
public interface DeptApi {

    /**
     * 根据 ID 获取部门信息（方法名避免与 CRUD 的 get 冲突）
     *
     * @param id 部门 ID
     * @return 部门信息
     */
    DeptResp getDeptInfo(Long id);
}