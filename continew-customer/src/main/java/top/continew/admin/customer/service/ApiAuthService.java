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

import top.continew.admin.customer.model.req.ApiDingTalkLoginReq;
import top.continew.admin.customer.model.req.ApiLoginReq;
import top.continew.admin.customer.model.resp.ApiUserInfoResp;

/**
 * 客户端认证服务
 *
 * @author weilai
 * @since 2026/01/15
 */
public interface ApiAuthService {

    /**
     * 手机号密码登录
     *
     * @param req 登录请求
     * @return 登录响应
     */
    String login(ApiLoginReq req);

    /**
     * 钉钉扫码登录
     *
     * @param req 钉钉登录请求
     * @return token
     */
    String dingTalkLogin(ApiDingTalkLoginReq req);

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    ApiUserInfoResp getUserInfo();

    /**
     * 登出
     */
    void logout();
}
