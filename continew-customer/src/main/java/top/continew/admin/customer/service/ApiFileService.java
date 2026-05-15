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

import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.customer.model.resp.ApiFileUploadResp;

import java.io.IOException;

/**
 * 客户端文件上传业务接口
 *
 * @author weilai
 * @since 2026/05/11
 */
public interface ApiFileService {

    /**
     * 上传文件
     *
     * @param file       文件
     * @param parentPath 上级目录
     * @return 文件上传响应
     * @throws IOException IO异常
     */
    ApiFileUploadResp upload(MultipartFile file, String parentPath) throws IOException;
}
