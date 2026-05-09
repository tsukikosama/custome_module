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

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import top.continew.admin.common.context.UserContext;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.customer.model.query.ApiProductQuery;
import top.continew.admin.customer.model.resp.ApiProductDetailResp;
import top.continew.admin.customer.model.resp.ApiProductResp;
import top.continew.admin.customer.service.ProductService;
import top.continew.admin.hrcommon.mapper.OrderMapper;
import top.continew.admin.hrcommon.mapper.ProductMapper;
import top.continew.admin.hrcommon.model.entity.OrderDO;
import top.continew.admin.hrcommon.model.entity.ProductDO;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 商品API业务实现
 *
 * @author weilai
 * @since 2026/01/15 10:32
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Override
    public PageResp<ApiProductResp> page(ApiProductQuery req) {
        // 构建查询条件
        QueryWrapper<ProductDO> wrapper = buildQueryWrapper(req);

        // 自动添加已上架条件
        wrapper.lambda().eq(ProductDO::getIsShelf, true);

        // 处理排序
        handleSort(wrapper, req);

        // 分页查询商品
        Page<ProductDO> page = new Page<>(req.getPage(), req.getSize());
        IPage<ProductDO> productPage = productMapper.selectPage(page, wrapper);

        // 转换为响应对象
        IPage<ApiProductResp> respPage = productPage.convert(product -> BeanUtil
            .copyProperties(product, ApiProductResp.class));

        return PageResp.build(respPage);
    }

    @Override
    public ApiProductDetailResp getDetail(Long id) {
        UserContext userContext = UserContextHolder.getContext();
        Long currentUserId = userContext.getId();

        ProductDO product = productMapper.selectById(id);
        CheckUtils.throwIfNull(product, "商品不存在");

        // 检查商品是否已上架
        CheckUtils.throwIf(!product.getIsShelf(), "商品已下架");

        // 转换基本信息
        ApiProductDetailResp resp = BeanUtil.copyProperties(product, ApiProductDetailResp.class);

        // 查询当前用户本月对该商品的购买次数
        Integer purchasedCount = getPurchaseCount(currentUserId, id);
        resp.setMonthlyPurchasedCount(purchasedCount);

        return resp;
    }

    /**
     * 查询用户本月对某商品的购买次数
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 购买次数
     */
    private Integer getPurchaseCount(Long userId, Long productId) {
        QueryWrapper<OrderDO> wrapper = Wrappers.query();
        wrapper.eq("user_id", userId);
        wrapper.eq("product_id", productId);
        wrapper.ne("status", 4); // 排除已取消的订单
        wrapper.apply("DATE_FORMAT(create_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')");

        OrderDO order = orderMapper.selectOne(wrapper);
        if (order == null) {
            return 0;
        }

        // 返回商品数量
        return order.getProductNum();
    }

    /**
     * 构建查询条件
     *
     * @param req 查询请求
     * @return 查询条件
     */
    private QueryWrapper<ProductDO> buildQueryWrapper(ApiProductQuery req) {
        QueryWrapper<ProductDO> wrapper = Wrappers.query();

        // 商品名称模糊查询
        if (req.getName() != null && !req.getName().isEmpty()) {
            wrapper.like("name", req.getName());
        }

        // 商品类型筛选
        if (req.getTypeId() != null) {
            wrapper.eq("type_id", req.getTypeId());
        }

        return wrapper;
    }

    /**
     * 处理排序
     *
     * @param wrapper 查询条件
     * @param req     查询参数
     */
    private void handleSort(QueryWrapper<ProductDO> wrapper, ApiProductQuery req) {
        String sortField = req.getSortField();
        String sortOrder = req.getSortOrder();

        if (sortField == null || sortField.isEmpty()) {
            // 默认按创建时间倒序
            wrapper.lambda().orderByDesc(ProductDO::getCreateTime);
            return;
        }

        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);

        if ("points".equals(sortField)) {
            wrapper.lambda().orderBy(true, isAsc, ProductDO::getPoints);
        } else if ("time".equals(sortField)) {
            wrapper.lambda().orderBy(true, isAsc, ProductDO::getCreateTime);
        } else {
            wrapper.lambda().orderByDesc(ProductDO::getCreateTime);
        }
    }
}
