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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import top.continew.admin.common.enums.DisEnableStatusEnum;
import top.continew.admin.hrcommon.mapper.StorageMapper;
import top.continew.admin.hrcommon.model.entity.StorageDO;
import top.continew.starter.core.util.SpringWebUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 文件存储配置
 *
 * @author weilai
 * @since 2026/05/11
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileStorageConfig implements ApplicationRunner {

    private final StorageMapper storageMapper;
    private final FileStorageService fileStorageService;

    @Override
    public void run(ApplicationArguments args) {
        List<StorageDO> list = storageMapper.lambdaQuery().eq(StorageDO::getStatus, DisEnableStatusEnum.ENABLE).list();
        if (CollUtil.isEmpty(list)) {
            log.warn("未找到启用的存储配置，文件上传功能可能无法正常使用");
            return;
        }

        list.forEach(this::load);
    }

    private void load(StorageDO storage) {
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        if (fileStorageList == null) {
            log.error("FileStorageService 的 fileStorageList 为 null，无法加载存储配置");
            return;
        }
        switch (storage.getType()) {
            case LOCAL -> {
                FileStorageProperties.LocalPlusConfig config = new FileStorageProperties.LocalPlusConfig();
                config.setPlatform(storage.getCode());
                config.setStoragePath(storage.getBucketName());
                // 确保 domain 以 / 结尾
                String domain = storage.getDomain();
                if (domain != null && !domain.endsWith("/")) {
                    domain = domain + "/";
                }
                config.setDomain(domain);
                fileStorageList.addAll(FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections
                    .singletonList(config)));
                // 注册资源映射
                SpringWebUtils.registerResourceHandler(MapUtil.of(URLUtil.url(domain).getPath(), storage
                    .getBucketName()));
                log.info("已加载本地存储：{} ({})", storage.getName(), storage.getCode());
            }
            case OSS -> {
                FileStorageProperties.AmazonS3Config config = new FileStorageProperties.AmazonS3Config();
                config.setPlatform(storage.getCode());
                config.setAccessKey(storage.getAccessKey());
                config.setSecretKey(storage.getSecretKey());
                config.setEndPoint(storage.getEndpoint());
                config.setBucketName(storage.getBucketName());
                config.setDomain(storage.getDomain());
                fileStorageList.addAll(FileStorageServiceBuilder.buildAmazonS3FileStorage(Collections
                    .singletonList(config), null));
                log.info("已加载对象存储：{} ({})", storage.getName(), storage.getCode());
            }
            default -> throw new IllegalArgumentException("不支持的存储类型：%s".formatted(storage.getType()));
        }
    }
}
