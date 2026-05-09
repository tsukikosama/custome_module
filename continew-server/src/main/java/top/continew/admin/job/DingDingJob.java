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

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.common.api.dingDingApi.DingTalkApiService;
import top.continew.admin.hrcommon.model.entity.OvertimeWorkDO;
import top.continew.admin.hrcommon.model.entity.StreamEventDO;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.system.model.req.user.UserPointChangeReq;
import top.continew.admin.system.service.OvertimeWorkService;
import top.continew.admin.system.service.StreamEventService;
import top.continew.admin.system.service.UserService;
import top.continew.starter.extension.tenant.annotation.TenantIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DingDingJob {

    private final StreamEventService streamEventService;

    private final OvertimeWorkService overtimeWorkService;

    private final DingTalkApiService dingTalkApiService;

    private final UserService userService;

    @TenantIgnore
    @JobExecutor(name = "OverWorkTimeToPoints")
    @Transactional(rollbackFor = Exception.class)
    public void publishNoticeWithScheduleJob() {
        //获取今日全部的申请时间
        List<StreamEventDO> todayEventRecord = streamEventService.getTodayAllRecord();

        //获取今日转换的加班记录
        List<OvertimeWorkDO> todayOvertimeWorkRecord = overtimeWorkService.getTodayOvertimeWorkRecord();

        // 获取加班记录中的全部processInstanceId
        Set<String> overtimeProcessInstanceIds = todayOvertimeWorkRecord.stream()
            .map(OvertimeWorkDO::getProcessInstanceId)
            .collect(Collectors.toSet());

        // 找出尚未转换为加班记录的申请记录（processInstanceIds中有但overtimeProcessInstanceIds中没有的）
        List<StreamEventDO> unprocessedRecords = todayEventRecord.stream()
            .filter(event -> !overtimeProcessInstanceIds.contains(event.getProcessInstanceId()))
            .toList();
        List<OvertimeWorkDO> overtimeWorkDOList = new ArrayList<>();
        //开始积分转换
        for (StreamEventDO unprocessedRecord : unprocessedRecords) {
            //获取到对应的事件
            String processInstanceId = unprocessedRecord.getProcessInstanceId();
            //判断这个时间是否已经处理过了
            if (!overtimeWorkDOList.isEmpty()) {
                //已经处理过了
                boolean exists = overtimeWorkDOList.stream()
                    .anyMatch(e -> e.getProcessInstanceId().equals(processInstanceId));
                if (exists) {
                    continue;
                }
            }
            //通过接口去获取详情信息
            JSONObject eventDetail = dingTalkApiService.getEventDetail(processInstanceId);
            if (eventDetail == null) {
                continue;
            }
            //判断一下是否是撤销的
            String action = eventDetail.getString("bizAction");
            //如果是revoke 和 MODIFY 状态标明这个是 撤销或者修改的
            if ("REVOKE".equals(action) || "MODIFY".equals(action)) {
                continue;
            }
            log.info("e{}", eventDetail);
            //判断是否是审批完成的
            String status = eventDetail.getString("status");
            log.info("审批状态{}", status);
            if (!"COMPLETED".equals(status)) {
                continue;
            }
            //如果是拒绝也不用继续下去了
            String result = eventDetail.getString("result");
            if ("refuse".equals(result)) {
                continue;
            }
            //获取到加班的开始时间结束时间和加班时长
            OvertimeWorkDO overtimeWorkDO = new OvertimeWorkDO();
            // 获取到审批用户
            String userId = eventDetail.getString("originatorUserId");
            overtimeWorkDO.setUserId(userService.getByDingDingId(userId).getId());
            JSONArray formComponentValues = eventDetail.getJSONArray("formComponentValues");
            for (int i = 0; i < formComponentValues.size(); i++) {
                JSONObject item = formComponentValues.getJSONObject(i);
                log.info("当前的value{}", item.toString());
                if ("DDDateRangeField".equals(item.getString("componentType"))) {
                    String value = item.getString("value");
                    // value = ["2026-01-19 10:00","2026-01-19 11:00","1"]
                    // 再解析一次
                    JSONArray arr = JSONArray.parseArray(value);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String startTime = arr.getString(0);
                    String endTime = arr.getString(1);
                    String days = arr.getString(2);
                    // 如果原始值只有到分钟，需要先补秒
                    if (startTime.length() == 16) {
                        startTime = startTime + ":00";
                    }
                    if (endTime.length() == 16) {
                        endTime = endTime + ":00";
                    }

                    overtimeWorkDO.setStartTime(LocalDateTime.parse(startTime, formatter));
                    overtimeWorkDO.setEndTime(LocalDateTime.parse(endTime, formatter));
                    overtimeWorkDO.setDuration(new BigDecimal(days));
                    overtimeWorkDO.setConvertPoints(new BigDecimal(days).multiply(BigDecimal.TEN)
                        .setScale(0, RoundingMode.DOWN)
                        .intValue());
                    // 你就可以保存或计算时长了
                    log.info("加班开始时间={}, 结束时间={}, 时长={}", startTime, endTime, days);
                    break;
                }
            }
            overtimeWorkDO.setStatus(status);
            overtimeWorkDO.setProcessInstanceId(processInstanceId);
            overtimeWorkDOList.add(overtimeWorkDO);
            overtimeWorkDO.setResult(result);
            overtimeWorkDO.setCreateUser(1L);
        }
        log.info("未转换的加班记录数量: {}", overtimeWorkDOList.size());
        if (!overtimeWorkDOList.isEmpty()) {
            overtimeWorkService.saveBatch(overtimeWorkDOList, overtimeWorkDOList.size());
        }
        for (OvertimeWorkDO overtimeWorkDO : overtimeWorkDOList) {
            UserPointChangeReq req = new UserPointChangeReq();
            req.setUserId(overtimeWorkDO.getUserId());
            req.setPoints(overtimeWorkDO.getConvertPoints());
            req.setType(PointsTypeEnum.INCREASE);

            //对用户进行加分
            userService.changePoints(req);
        }

    }
}
