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

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.customer.model.resp.ApiFileUploadResp;
import top.continew.admin.customer.service.ApiFileService;
import top.continew.admin.common.mapper.StorageMapper;
import top.continew.admin.common.model.entity.StorageDO;
import top.continew.starter.core.constant.StringConstants;

import java.time.LocalDate;

/**
 * 客户端文件上传业务实现
 *
 * @author weilai
 * @since 2026/05/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiFileServiceImpl implements ApiFileService {

    private final FileStorageService fileStorageService;
    private final StorageMapper storageMapper;

    @Override
    public ApiFileUploadResp upload(MultipartFile file, String parentPath) {
        String path = pretreatmentPath(parentPath);
        StorageDO defaultStorage = getDefaultStorage();
        String platform = defaultStorage.getCode();

        log.info("准备上传文件：{}，大小：{}", file.getOriginalFilename(), file.getSize());
        log.info("使用存储平台：{} ({})", platform, defaultStorage.getName());
        log.info("存储路径：{}, domain={}", defaultStorage.getBucketName(), defaultStorage.getDomain());
        log.info("FileStorageService实例：{}", fileStorageService.hashCode());

        // 检查当前实例中的存储平台
        if (fileStorageService.getFileStorageList() != null) {
            log.info("当前实例已加载的存储平台数量：{}", fileStorageService.getFileStorageList().size());
            fileStorageService.getFileStorageList().forEach(fs -> log.info("  - 平台code：{}", fs.getPlatform()));
        }

        FileInfo fileInfo = fileStorageService.of(file)
            .setPlatform(platform)
            .setPath(path)
            .setHashCalculatorSha256(true)
            .upload();

        log.info("文件上传成功，URL：{}", fileInfo.getUrl());

        return ApiFileUploadResp.builder()
            .fileName(fileInfo.getOriginalFilename())
            .url(fileInfo.getUrl())
            .size(fileInfo.getSize())
            .build();
    }

    private StorageDO getDefaultStorage() {
        StorageDO storage = storageMapper.lambdaQuery().eq(StorageDO::getIsDefault, true).one();
        if (storage == null) {
            storage = storageMapper.lambdaQuery()
                .eq(StorageDO::getStatus, top.continew.admin.common.enums.DisEnableStatusEnum.ENABLE)
                .last("LIMIT 1")
                .one();
        }
        if (storage == null) {
            throw new RuntimeException("未找到可用的存储配置");
        }
        return storage;
    }

    private String pretreatmentPath(String path) {
        if (StrUtil.isBlank(path)) {
            LocalDate today = LocalDate.now();
            return today.getYear() + StringConstants.SLASH + today.getMonthValue() + StringConstants.SLASH + today
                .getDayOfMonth() + StringConstants.SLASH;
        }
        if (StringConstants.SLASH.equals(path)) {
            return StringConstants.EMPTY;
        }
        return StrUtil.appendIfMissing(StrUtil.removePrefix(path, StringConstants.SLASH), StringConstants.SLASH);
    }
}
