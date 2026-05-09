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

package top.continew.admin.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传配置
 *
 * @author weilai
 * @since 2026/05/08
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上传文件存储路径
     */
    private String path = "uploads/";

    /**
     * 访问URL前缀
     */
    private String urlPrefix = "/uploads/";

    /**
     * 完整访问URL前缀（用于返回给前端的完整URL）
     */
    private String fullUrlPrefix;

    /**
     * 支持的文件后缀（图片、文档、压缩包等常见文件类型）
     */
    private String supportSuffix = "jpg,jpeg,png,gif,bmp,webp,pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar,7z,mp3,mp4,avi,mov";

    /**
     * 最大文件大小（默认10MB）
     */
    private Long maxFileSize = 10L * 1024 * 1024;
}
