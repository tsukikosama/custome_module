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

import top.continew.admin.common.base.model.resp.BaseDetailResp;

import java.io.Serial;

/**
 * 商品详情信息
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Data
@Schema(description = "商品详情信息")
public class ApiProductDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "机械键盘")
    private String name;

    /**
     * 商品图片
     */
    @Schema(description = "商品图片", example = "https://example.com/images/keyboard.jpg")
    private String image;

    /**
     * 所需积分
     */
    @Schema(description = "所需积分", example = "500")
    private Integer points;

    /**
     * 商品类型ID
     */
    @Schema(description = "商品类型ID", example = "1")
    private Long typeId;

    /**
     * 商品描述
     */
    @Schema(description = "商品描述", example = "机械键盘，红轴设计")
    private String description;

    /**
     * 每月限兑次数，null表示不限次数
     */
    @Schema(description = "每月限兑次数，null表示不限次数", example = "2")
    private Integer monthLimit;

    /**
     * 当前用户本月已购买该商品的次数（自然月）
     */
    @Schema(description = "当前用户本月已购买该商品的次数", example = "1")
    private Integer monthlyPurchasedCount;
}
