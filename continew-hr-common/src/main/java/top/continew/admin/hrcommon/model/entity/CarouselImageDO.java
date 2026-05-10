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

import top.continew.admin.common.base.model.entity.BaseCreateDO;

import java.io.Serial;

/**
 * 轮播图实体
 *
 * @author weilai
 * @since 2026/05/09 09:37
 */
@Data
@TableName("biz_carousel_image")
public class CarouselImageDO extends BaseCreateDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 轮播图标题
     */
    private String title;

    /**
     * 跳转路径
     */
    private String jumpPath;

    /**
     * 轮播图内容
     */
    private String content;
}
