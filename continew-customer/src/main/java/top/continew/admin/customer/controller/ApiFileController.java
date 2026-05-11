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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.customer.model.resp.ApiFileUploadResp;
import top.continew.admin.customer.service.ApiFileService;
import top.continew.starter.core.util.validation.ValidationUtils;
import top.continew.starter.log.annotation.Log;

import java.io.IOException;

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
@RequestMapping("/api/file")
public class ApiFileController {

    private final ApiFileService apiFileService;

    @Operation(summary = "上传文件", description = "上传文件（支持图片、文档、压缩包等常见格式，自动生成缩略图）")
    @Parameter(name = "parentPath", description = "上级目录（默认：yyyy/MM/dd/）", example = "/", in = ParameterIn.QUERY)
    @PostMapping("/upload")
    public ApiFileUploadResp upload(@RequestPart @NotNull(message = "文件不能为空") MultipartFile file,
                                    @RequestParam(required = false) String parentPath) throws IOException {
        ValidationUtils.throwIf(file::isEmpty, "文件不能为空");
        return apiFileService.upload(file, parentPath);
    }
}
