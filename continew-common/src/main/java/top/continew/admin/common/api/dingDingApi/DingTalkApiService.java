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

package top.continew.admin.common.api.dingDingApi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkcontact_1_0.Client;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskHeaders;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import top.continew.admin.common.api.dingDingApi.request.GetDeptIdReq;
import top.continew.admin.common.api.dingDingApi.request.GetDeptUserInfoReq;
import top.continew.admin.common.api.dingDingApi.request.SendMessageReq;
import top.continew.admin.common.api.dingDingApi.response.UserInfoResp;
import top.continew.starter.core.exception.BusinessException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DingTalkApiService {

    private final DingTalkProperties dingTalkProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String DINGDING_ACCESS_TOKEN_KEY = "dingding:accessToken";

    /**
     * 获取accessToken
     *
     * @param authCode
     * @return
     */
    public String getAccessToken(String authCode) {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            com.aliyun.dingtalkoauth2_1_0.Client client = new com.aliyun.dingtalkoauth2_1_0.Client(config);
            GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest().setClientId(dingTalkProperties
                .getClientId())
                .setClientSecret(dingTalkProperties.getClientSecret())
                .setCode(authCode)
                .setGrantType("authorization_code");
            GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
            return getUserTokenResponse.getBody().getAccessToken();
        } catch (TeaException e) {
            log.error("获取钉钉accessToken失败，钉钉异常: code={}, message={}, 详细信息:{}", e.getCode(), e.getMessage(), e
                .getData(), e);
            throw new BusinessException("获取钉钉accessToken失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("获取钉钉accessToken失败，系统异常: {}", e.getMessage(), e);
            throw new BusinessException("获取钉钉accessToken失败: " + e.getMessage());
        }
    }

    /**
     * 获取钉钉用户信息
     * 
     * @param accessToken accessToken
     * @return 用户信息或错误信息
     */
    public ResponseEntity<JSONObject> getUserInfo(String accessToken) {
        Client client = getClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        try {
            GetUserResponse resp = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions());
            JSONObject user = new JSONObject();
            user.put("unionId", resp.getBody().getUnionId());
            user.put("phone", resp.getBody().getMobile());
            user.put("nick", resp.getBody().getNick());
            user.put("stateCode", resp.getBody().getStateCode());
            return ResponseEntity.ok(user);
        } catch (TeaException err) {
            JSONObject error = new JSONObject();
            error.put("error", "钉钉接口异常: " + err.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
        } catch (Exception err) {
            JSONObject error = new JSONObject();
            error.put("error", "未知异常: " + err.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * 通过钉钉授权码获取用户信息
     * 
     * @param authCode 授权码
     * @return 用户信息或错误信息
     */
    public ResponseEntity<JSONObject> getUsers(String authCode) {
        String accessToken = getAccessToken(authCode);
        ResponseEntity<JSONObject> userInfoResp = getUserInfo(accessToken);
        return userInfoResp;
    }

    public Client getClient() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        Client client;
        try {
            client = new Client(config);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", "初始化钉钉Client失败: " + e.getMessage());
            throw new BusinessException("初始化钉钉Client失败");
        }
        return client;
    }

    /**
     * 获取企业内部token
     *
     * @return
     */
    public String getAccessToken() {

        // 先从Redis获取
        String token = Optional.ofNullable(redisTemplate.opsForValue().get(DINGDING_ACCESS_TOKEN_KEY)).orElse("");
        if (StrUtil.isNotBlank(token)) {
            log.info("从Redis获取access_token成功");
            return token; // Redis 有直接返回
        }

        log.info("Redis中无access_token，开始从钉钉服务器获取...");
        try {
            HttpRequest request = HttpUtil.createGet("https://oapi.dingtalk.com/gettoken");

            request.form("appkey", dingTalkProperties.getClientId());
            request.form("appsecret", dingTalkProperties.getClientSecret());
            HttpResponse response = request.execute();
            String body = response.body();

            log.info("获取钉钉access_token响应: {}", body);

            JSONObject jsonObject = JSON.parseObject(body);
            Integer errcode = jsonObject.getInteger("errcode");

            if (errcode != null && errcode == 0) {
                token = jsonObject.getString("access_token");

                if (StrUtil.isNotBlank(token)) {
                    // 存储到Redis，6900秒后过期
                    redisTemplate.opsForValue().set(DINGDING_ACCESS_TOKEN_KEY, token, 6900, TimeUnit.SECONDS);
                    log.info("获取access_token成功并存储到Redis");
                } else {
                    log.error("获取access_token失败：响应中access_token为空");
                }
            } else {
                log.error("获取access_token失败：errcode={}, errmsg={}", errcode, jsonObject.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("获取accessToken失败: {}", e.getMessage(), e);
        }

        log.info("最终返回的access_token: {}", token != null ? token : "null或空字符串");
        return token;
    }

    public JSONArray getDepartmentJson() {
        try {
            GetDeptIdReq req = new GetDeptIdReq();
            req.setDept_id(1L);
            req.setLanguage("zh_CN");
            Map<String, Object> stringObjectMap = beanToMap(req);
            JSONObject jsonObject = doGet("https://oapi.dingtalk.com/topapi/v2/department/listsub", stringObjectMap);
            log.info("jsonObject{}", jsonObject);
            JSONArray result = jsonObject.getJSONArray("result");
            return result;
        } catch (Exception e) {
            log.error("获取部门列表信息失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取部门列表
     */
    public List<Long> getDepartmentList() {
        try {
            GetDeptIdReq req = new GetDeptIdReq();
            req.setDept_id(1L);
            req.setLanguage("zh_CN");
            Map<String, Object> stringObjectMap = beanToMap(req);
            JSONObject jsonObject = doGet("https://oapi.dingtalk.com/topapi/v2/department/listsubid", stringObjectMap);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray deptIdArray = result.getJSONArray("dept_id_list");
            return deptIdArray.toJavaList(Long.class);
        } catch (Exception e) {
            log.error("获取部门列表信息失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取指定部门的人员信息
     * 
     * @return
     */
    public List<UserInfoResp> getAllUserInfo(Long deptId) {
        try {
            GetDeptUserInfoReq req = new GetDeptUserInfoReq();

            req.setDept_id(deptId);
            req.setCursor(0);
            req.setSize(100);
            Map<String, Object> stringObjectMap = beanToMap(req);
            JSONObject jsonObject = doGet("https://oapi.dingtalk.com/topapi/v2/user/list", stringObjectMap);

            JSONArray result = jsonObject.getJSONObject("result").getJSONArray("list");
            return jsonArrayToList(result, UserInfoResp.class);
        } catch (Exception e) {
            log.error("获取部门人员信息失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 查询详情
     * 
     * @param processInstanceId 事件id
     * @return
     */
    public JSONObject getEventDetail(String processInstanceId) {
        Map<String, Object> map = new HashMap<>();
        map.put("processInstanceId", processInstanceId);
        JSONObject jsonObject = doGetV1("https://api.dingtalk.com/v1.0/workflow/processInstances", map);
        log.info("查询详情返回结果:{}", jsonObject);
        return jsonObject.getJSONObject("result");

    }

    public void sendMessage(CreateTodoTaskRequest req) throws Exception {
        try {
            // 记录请求参数
            log.info("创建钉钉个人待办任务请求参数: {}", JSON.toJSONString(req));

            // 创建钉钉待办任务客户端
            com.aliyun.dingtalktodo_1_0.Client client = createTodoClient();

            // 设置请求头
            CreateTodoTaskHeaders headers = new CreateTodoTaskHeaders();
            headers.xAcsDingtalkAccessToken = getAccessToken();

            // 调用创建个人待办任务API
            client.createTodoTaskWithOptions(String.valueOf(dingTalkProperties
                .getAgentId()), req, headers, new RuntimeOptions());

            log.info("创建钉钉个人待办任务成功");
        } catch (TeaException err) {
            log.error("创建钉钉个人待办任务失败，钉钉接口异常: {}", err.getMessage());
            log.error("错误详情: {}", JSON.toJSONString(err));
            throw new BusinessException("创建钉钉个人待办任务失败: " + err.getMessage());
        } catch (Exception err) {
            log.error("创建钉钉个人待办任务失败，未知异常: {}", err.getMessage());
            log.error("错误详情: {}", err.getMessage());
            throw new BusinessException("创建钉钉个人待办任务失败: " + err.getMessage());
        }
    }

    /**
     * 创建钉钉待办任务客户端
     * 
     * @return 钉钉待办任务客户端
     */
    private com.aliyun.dingtalktodo_1_0.Client createTodoClient() {
        try {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";

            return new com.aliyun.dingtalktodo_1_0.Client(config);
        } catch (Exception e) {
            log.error("创建钉钉待办任务客户端失败: {}", e.getMessage());
            throw new BusinessException("创建钉钉待办任务客户端失败");
        }
    }

    /**
     * 旧版本api
     * 
     * @param url
     * @param params
     * @return
     */
    public JSONObject doGet(String url, Map<String, Object> params) {
        log.info("请求的路径{}，请求的参数{}", url, params);
        try {
            HttpRequest request = HttpUtil.createGet(url);

            if (params != null && !params.isEmpty()) {
                params.forEach((k, v) -> {
                    if (v != null) {
                        request.form(k, v.toString());
                    }
                });
            }
            String token = getAccessToken();
            request.form("access_token", token);

            HttpResponse response = request.execute();
            String body = response.body();
            if (StrUtil.isBlank(body)) {
                return new JSONObject();
            }

            return JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET 请求失败：" + url, e);
        }
    }

    public JSONObject doPost(String url, Object params) {
        log.info("请求的路径{}，请求的参数{}", url, params);
        try {
            HttpRequest request = HttpUtil.createPost(url);

            String token = getAccessToken();
            request.header("x-acs-dingtalk-access-token", token);
            request.header("Content-Type", "application/json");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(params);
            request.body(json);
            // 手动打印请求信息
            System.out.println("====== HTTP REQUEST ======");
            System.out.println(request.getMethod() + " " + request.getUrl());
            request.headers().forEach((k, v) -> {
                System.out.println(k + ": " + v);
            });
            System.out.println();
            System.out.println(json);
            System.out.println("==========================");
            HttpResponse response = request.execute();
            String body = response.body();
            return JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("HTTP Post 请求失败：" + url, e);
        }
    }

    /**
     * 旧版本API POST请求（通过URL参数传递access_token）
     *
     * @param url
     * @param params
     * @return
     */
    public JSONObject doPostV2(String url, Object params) {
        log.info("请求的路径{}，请求的参数{}", url, params);
        try {
            String token = getAccessToken();

            // 将access_token添加到URL中
            String fullUrl = url + "?access_token=" + token;
            HttpRequest request = HttpUtil.createPost(fullUrl);

            request.header("Content-Type", "application/json");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(params);
            request.body(json);

            // 手动打印请求信息
            System.out.println("====== HTTP REQUEST ======");
            System.out.println(request.getMethod() + " " + request.getUrl());
            request.headers().forEach((k, v) -> {
                System.out.println(k + ": " + v);
            });
            System.out.println();
            System.out.println(json);
            System.out.println("==========================");

            HttpResponse response = request.execute();
            String body = response.body();

            log.info("钉钉API响应: {}", body);

            return JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("HTTP Post 请求失败：" + url, e);
        }
    }

    /**
     * 新版本api
     * 
     * @param url
     * @param params
     * @return
     */
    public JSONObject doGetV1(String url, Map<String, Object> params) {
        log.info("请求的路径{}，请求的参数{}", url, params);
        try {
            HttpRequest request = HttpUtil.createGet(url);

            if (params != null && !params.isEmpty()) {
                params.forEach((k, v) -> {
                    if (v != null) {
                        request.form(k, v.toString());
                    }
                });
            }
            String token = getAccessToken();
            request.header("x-acs-dingtalk-access-token", token);
            request.header("Content-Type", "application/json");

            HttpResponse response = request.execute();
            String body = response.body();
            if (StrUtil.isBlank(body)) {
                return new JSONObject();
            }

            return JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET 请求失败：" + url, e);
        }
    }

    public Map<String, Object> beanToMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = bean.getClass();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(bean);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("对象转 Map 失败", e);
        }
        return map;
    }

    /**
     * 将 JSONArray 转换为指定 Java 对象 List
     * 
     * @param array JSONArray
     * @param clazz 目标对象类
     * @param <T>   类型
     * @return List<T>
     */
    public static <T> List<T> jsonArrayToList(JSONArray array, Class<T> clazz) {
        if (array == null || array.isEmpty()) {
            return List.of();
        }
        return array.toJavaList(clazz);
    }

    public List<Long> getSubDept(Long deptId) {
        try {
            GetDeptIdReq req = new GetDeptIdReq();
            req.setDept_id(deptId);
            Map<String, Object> stringObjectMap = beanToMap(req);
            JSONObject jsonObject = doGet("https://oapi.dingtalk.com/topapi/v2/department/listsubid", stringObjectMap);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray deptIdArray = result.getJSONArray("dept_id_list");
            return deptIdArray.toJavaList(Long.class);
        } catch (Exception e) {
            log.error("获取部门列表信息失败:{}", e.getMessage());
        }
        return null;
    }

    public JSONObject getSubDeptInfo(Long deptId) {
        try {
            GetDeptIdReq req = new GetDeptIdReq();
            req.setDept_id(deptId);
            Map<String, Object> stringObjectMap = beanToMap(req);
            JSONObject jsonObject = doGet("https://oapi.dingtalk.com/topapi/v2/department/get", stringObjectMap);
            JSONObject result = jsonObject.getJSONObject("result");

            return result;
        } catch (Exception e) {
            log.error("获取部门列表信息失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 发送企业会话消息（旧版API，需要通过URL参数传递access_token）
     *
     * @param sendMessageReq 发送消息请求
     * @return 响应结果
     */
    public JSONObject sendConversationMessage(SendMessageReq sendMessageReq) {
        String url = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
        return doPostV2(url, sendMessageReq);
    }
}
