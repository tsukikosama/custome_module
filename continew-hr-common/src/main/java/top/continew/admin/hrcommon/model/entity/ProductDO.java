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

package top.continew.admin.hrcommon.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import top.continew.admin.common.base.model.entity.BaseDO;
import top.continew.starter.extension.crud.annotation.DictModel;

import java.io.Serial;

/**
 * 商品表实体
 *
 * @author weilai
 * @since 2026/01/14 18:00
 */
@Data
@TableName("biz_product")
@DictModel(valueKey = "id")
public class ProductDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品积分
     */
    private Integer points;

    /**
     * 商品类型ID
     */
    private Long typeId;

    /**
     * 商品描述
     */
    private String description;

    private Boolean isShelf;

    private Integer monthLimit;
}
