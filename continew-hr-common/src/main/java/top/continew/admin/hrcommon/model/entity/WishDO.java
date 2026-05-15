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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import top.continew.admin.hrcommon.model.enums.WishStatusEnum;
import top.continew.starter.extension.crud.annotation.DictModel;
import top.continew.starter.extension.crud.model.entity.BaseIdDO;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 心愿表实体
 *
 * @author weilai
 * @since 2026/01/20 17:23
 */
@Data
@TableName("biz_wish")
@DictModel
public class WishDO extends BaseIdDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 转成商品的id
     */
    private Long productId;

    /**
     * 是否转成商品
     */
    private Boolean isProduct;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否已删除（0：否；id：是）
     */
    @TableLogic(value = "0", delval = "id")
    private Long deleted;

    /**
     * 父id 如果为0 代表是父
     */
    private Long parentsId;

    /**
     * 许愿状态 1-心愿中 2-许愿成功 3-许愿失败 4-许愿取消
     */
    private WishStatusEnum status;

    /**
     * 失败原因
     */
    private String failReason;
}
