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

package top.continew.admin.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.customer.service.CarouselService;
import top.continew.admin.hrcommon.model.resp.ApiCarouselResp;
import top.continew.starter.log.annotation.Log;

import java.util.List;

/**
 * 轮播图 API
 *
 * @author weilai
 * @since 2026/05/09
 */
@Tag(name = "轮播图 API")
@Log(module = "轮播图管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carousel")
public class ApiCarouselController {

    private final CarouselService carouselService;

    @Log(ignore = true)
    @Operation(summary = "查询轮播图列表", description = "查询所有轮播图列表，按顺序升序排序")
    @GetMapping
    public List<ApiCarouselResp> list() {
        return carouselService.list();
    }
}
