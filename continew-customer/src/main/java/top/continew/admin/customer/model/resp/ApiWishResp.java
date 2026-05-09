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

package top.continew.admin.customer.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 客户端心愿信息
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Data
@Schema(description = "客户端心愿信息")
public class ApiWishResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 心愿ID
     */
    @Schema(description = "心愿ID")
    private Long id;

    /**
     * 心愿名称
     */
    @Schema(description = "心愿名称")
    private String title;

    /**
     * 提交时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "提交时间")
    private String createTime;

    /**
     * 许愿状态
     */
    @Schema(description = "许愿状态")
    private Integer status;

    /**
     * 许愿状态文本描述
     */
    @Schema(description = "许愿状态文本描述")
    private String statusText;

    /**
     * 是否已转成商品
     */
    @Schema(description = "是否已转成商品")
    private Boolean isProduct;

    /**
     * 推荐理由
     */
    @Schema(description = "推荐理由")
    private String reason;
}
