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

package top.continew.admin.job;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.system.service.UserService;
import top.continew.starter.extension.tenant.annotation.TenantIgnore;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.system.model.req.user.UserPointChangeReq;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointsJob {

    private final UserService userService;

    @TenantIgnore
    @JobExecutor(name = "clearPoints")
    @Transactional(rollbackFor = Exception.class)
    public void clearPoints() {
        log.info("开始执行清零所有用户积分任务");

        // 查询所有用户
        List<UserDO> userList = userService.list(Wrappers.<UserDO>lambdaQuery()
            .isNotNull(UserDO::getPoints)
            .gt(UserDO::getPoints, 0));

        log.info("找到需要清零积分的用户数量: {}", userList.size());

        // 批量更新用户积分为0
        for (UserDO user : userList) {
            // 创建积分变更请求
            UserPointChangeReq pointChangeReq = new UserPointChangeReq();
            pointChangeReq.setUserId(user.getId());
            pointChangeReq.setPoints(user.getPoints());
            pointChangeReq.setType(PointsTypeEnum.DECREASE);

            // 调用积分变更方法
            userService.changePoints(pointChangeReq);
        }

        log.info("清零所有用户积分任务执行完成");
    }

}
