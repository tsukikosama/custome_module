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

package top.continew.admin.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.customer.config.FileUploadProperties;
import top.continew.admin.customer.model.resp.ApiFileUploadResp;
import top.continew.starter.core.util.validation.ValidationUtils;
import top.continew.starter.log.annotation.Log;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件上传 API
 *
 * @author weilai
 * @since 2026/05/08
 */
@Tag(name = "文件上传 API")
@Log(module = "文件上传")
@Validated
@RestController
@RequiredArgsConstructor
@EnableConfigurationProperties(FileUploadProperties.class)
@RequestMapping("/api/file")
public class ApiFileController {

    private final FileUploadProperties fileUploadProperties;

    @Operation(summary = "上传文件", description = "上传文件（支持图片、文档、压缩包等常见格式，最大10MB）")
    @Parameter(name = "file", description = "文件", required = true, in = ParameterIn.QUERY)
    @PostMapping("/upload")
    public ApiFileUploadResp upload(@RequestPart @NotNull(message = "文件不能为空") MultipartFile file) throws IOException {
        // 验证文件不为空
        ValidationUtils.throwIf(file::isEmpty, "文件不能为空");

        // 验证文件大小
        ValidationUtils.throwIf(file.getSize() > fileUploadProperties
            .getMaxFileSize(), "文件大小不能超过 " + (fileUploadProperties.getMaxFileSize() / 1024 / 1024) + "MB");

        // 获取文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        ValidationUtils.throwIfBlank(originalFilename, "文件名不能为空");

        String fileExtension = getFileExtension(originalFilename);

        // 验证文件类型
        Set<String> supportedExtensions = new HashSet<>(Arrays.asList(fileUploadProperties.getSupportSuffix()
            .split(",")));
        ValidationUtils.throwIf(!supportedExtensions.contains(fileExtension
            .toLowerCase()), "只支持以下文件格式: " + fileUploadProperties.getSupportSuffix());

        // 构建存储路径（按日期分层）
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = datePath + "/" + System.currentTimeMillis() + "_" + generateFileName(originalFilename);
        String fullPath = fileUploadProperties.getPath() + relativePath;

        // 确保目录存在
        File destFile = new File(fullPath);
        destFile.getParentFile().mkdirs();

        // 保存文件
        file.transferTo(destFile);

        // 构建访问URL（使用配置的完整URL前缀）
        String accessUrl;
        if (fileUploadProperties.getFullUrlPrefix() != null && !fileUploadProperties.getFullUrlPrefix().isEmpty()) {
            // 使用配置的完整URL前缀
            accessUrl = fileUploadProperties.getFullUrlPrefix() + relativePath.replace("\\", "/");
        } else {
            // 回退到相对路径前缀
            accessUrl = fileUploadProperties.getUrlPrefix() + relativePath.replace("\\", "/");
        }

        return ApiFileUploadResp.builder().fileName(destFile.getName()).url(accessUrl).size(file.getSize()).build();
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * 生成安全的文件名（去除特殊字符）
     *
     * @param originalFilename 原始文件名
     * @return 安全的文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        // 去除文件名中的特殊字符，只保留字母、数字、下划线、连字符
        return baseName.replaceAll("[^a-zA-Z0-9_\\-\\u4e00-\\u9fa5]", "_") + "." + extension;
    }
}
