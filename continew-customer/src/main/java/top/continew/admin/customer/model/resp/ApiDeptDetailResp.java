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

package top.continew.admin.customer.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 部门详情响应对象
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Schema(description = "部门详情响应对象")
public class ApiDeptDetailResp {

    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门描述")
    private String description;

    @Schema(description = "部门成员列表")
    private List<DeptMemberResp> members;

    /**
     * 部门成员响应对象
     */
    @Data
    @Schema(description = "部门成员响应对象")
    public static class DeptMemberResp {

        @Schema(description = "用户ID")
        private Long userId;

        @Schema(description = "用户昵称")
        private String nickname;

        @Schema(description = "头像")
        private String avatar;

        @Schema(description = "手机号")
        private String phone;

        @Schema(description = "描述")
        private String description;

        @Schema(description = "职位")
        private String jobTitle;
    }
}
