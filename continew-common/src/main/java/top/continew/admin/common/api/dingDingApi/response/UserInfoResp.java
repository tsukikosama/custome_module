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

package top.continew.admin.common.api.dingDingApi.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserInfoResp {
    private boolean leader;
    private Map<String, Object> extension; // JSON 字符串可以转成 Map
    private String unionid;
    private boolean boss;
    private boolean exclusiveAccount;
    private boolean admin;
    private String remark;
    private String title;
    @JSONField(name = "hired_date")
    private Long hiredDate; // 时间戳
    private String userid;
    private String workPlace;
    @JSONField(name = "dept_id_list")
    private List<Long> deptIdList; // JSON 数组
    private String jobNumber;
    private String email;
    @JSONField(name = "dept_order")
    private String deptOrder;
    private String mobile;
    private boolean active;
    private String telephone;
    private String avatar;
    @JSONField(name = "hide_mobile")
    private boolean hideMobile;
    private String orgEmail;
    private String name;
    private String stateCode;

}
