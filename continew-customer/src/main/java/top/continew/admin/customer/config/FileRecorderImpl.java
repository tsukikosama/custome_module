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

import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Component;

/**
 * 文件记录实现类（客户端简化版）
 *
 * @author weilai
 * @since 2026/05/11
 */
@Slf4j
@Component
public class FileRecorderImpl implements FileRecorder {

    @Override
    public boolean save(FileInfo fileInfo) {
        log.debug("文件上传成功：{}", fileInfo.getOriginalFilename());
        return true;
    }

    @Override
    public FileInfo getByUrl(String url) {
        // 客户端不需要查询文件记录
        return null;
    }

    @Override
    public boolean delete(String url) {
        log.debug("文件删除：{}", url);
        return true;
    }

    @Override
    public void update(FileInfo fileInfo) {
        log.debug("文件记录更新：{}", fileInfo.getOriginalFilename());
    }

    @Override
    public void deleteFilePartByUploadId(String uploadId) {
        log.debug("删除分片上传记录：{}", uploadId);
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {
        log.debug("保存分片上传记录：{}", filePartInfo.getUploadId());
    }
}
