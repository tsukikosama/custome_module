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

package top.continew.admin.system.model.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.common.model.enums.PointsTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户积分批量导入请求参数
 *
 * @author weilai
 * @since 2026/01/16
 */
@Data
@Schema(description = "用户积分批量导入请求参数")
public class UserPointImportReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 积分类型
     */
    @Schema(description = "积分类型", example = "1")
    @NotNull(message = "积分类型不能为空")
    private PointsTypeEnum type;

    /**
     * 来源ID
     */
    @Schema(description = "来源ID", example = "1")
    @NotNull(message = "来源ID不能为空")
    private Long refId;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "活动奖励")
    private String remark;

    /**
     * 导入文件
     */
    @NotNull(message = "导入文件不能为空")
    private MultipartFile file;
}
