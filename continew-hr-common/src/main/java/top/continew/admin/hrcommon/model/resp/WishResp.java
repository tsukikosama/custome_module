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

package top.continew.admin.hrcommon.model.resp;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.admin.common.base.model.resp.BaseResp;
import top.continew.admin.hrcommon.model.enums.WishStatusEnum;

import java.io.Serial;
import java.time.*;

/**
 * 心愿表信息
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Data
@Schema(description = "心愿表信息")
public class WishResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String name;

    /**
     * 转成商品的id
     */
    @Schema(description = "转成商品的id")
    private Long productId;

    /**
     * 是否转成商品
     */
    @Schema(description = "是否转成商品")
    private Boolean isProduct;

    /**
     * 父id
     */
    @Schema(description = "父id")
    private Long parentsId;

    /**
     * 许愿状态 1-心愿中 2-许愿成功 3-许愿失败 4-许愿取消
     */
    @Schema(description = "许愿状态")

    private WishStatusEnum status;

    /**
     * 失败原因
     */
    @Schema(description = "失败原因")
    private String failReason;

    /**
     * 子心愿数量
     */
    @Schema(description = "子心愿数量")
    private Integer childrenCount;

    @Schema(description = "许愿用户")
    private String wishUserNames;
}
