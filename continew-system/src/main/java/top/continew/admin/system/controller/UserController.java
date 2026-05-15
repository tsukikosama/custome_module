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

package top.continew.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.common.util.SecureUtils;
import top.continew.admin.hrcommon.model.enums.PointsTypeEnum;
import top.continew.admin.hrcommon.model.entity.user.UserDO;
import top.continew.admin.hrcommon.model.resp.user.UserDetailResp;
import top.continew.admin.system.model.query.UserQuery;
import top.continew.admin.system.model.req.user.UserImportReq;
import top.continew.admin.system.model.req.user.UserPasswordResetReq;
import top.continew.admin.system.model.req.user.UserPointChangeReq;
import top.continew.admin.system.model.req.user.UserReq;
import top.continew.admin.system.model.req.user.UserPointImportReq;
import top.continew.admin.system.model.req.user.UserPushMessageUpdateReq;
import top.continew.admin.system.model.req.user.UserRoleUpdateReq;
import top.continew.admin.system.model.resp.user.UserImportParseResp;
import top.continew.admin.system.model.resp.user.UserImportResp;
import top.continew.admin.system.model.resp.user.UserPointImportResp;
import top.continew.admin.system.model.resp.user.UserResp;
import top.continew.admin.system.service.UserService;
import top.continew.starter.core.util.validation.ValidationUtils;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

import java.io.IOException;
import java.util.List;

/**
 * 用户管理 API
 *
 * @author Charles7c
 * @since 2023/2/20 21:00
 */
@Tag(name = "用户管理 API")
@Validated
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/system/user", api = {Api.PAGE, Api.LIST, Api.GET, Api.CREATE, Api.UPDATE,
    Api.BATCH_DELETE, Api.EXPORT, Api.DICT})
public class UserController extends BaseController<UserService, UserResp, UserDetailResp, UserQuery, UserReq> {

    @Operation(summary = "下载导入模板", description = "下载导入模板")
    @SaCheckPermission("system:user:import")
    @GetMapping(value = "/import/template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        baseService.downloadImportTemplate(response);
    }

    @Operation(summary = "解析导入数据", description = "解析导入数据")
    @SaCheckPermission("system:user:import")
    @PostMapping("/import/parse")
    public UserImportParseResp parseImport(@RequestPart @NotNull(message = "文件不能为空") MultipartFile file) {
        ValidationUtils.throwIf(file::isEmpty, "文件不能为空");
        return baseService.parseImport(file);
    }

    @Operation(summary = "导入数据", description = "导入数据")
    @SaCheckPermission("system:user:import")
    @PostMapping(value = "/import")
    public UserImportResp importUser(@RequestBody @Valid UserImportReq req) {
        return baseService.importUser(req);
    }

    @Operation(summary = "重置密码", description = "重置用户登录密码")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:resetPwd")
    @PatchMapping("/{id}/password")
    public void resetPassword(@RequestBody @Valid UserPasswordResetReq req, @PathVariable Long id) {
        String newPassword = SecureUtils.decryptPasswordByRsaPrivateKey(req.getNewPassword(), "新密码解密失败", true);
        req.setNewPassword(newPassword);
        baseService.resetPassword(req, id);
    }

    @Operation(summary = "分配角色", description = "为用户新增或移除角色")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:updateRole")
    @PatchMapping("/{id}/role")
    public void updateRole(@RequestBody @Valid UserRoleUpdateReq updateReq, @PathVariable Long id) {
        baseService.updateRole(updateReq, id);
    }

    @Operation(summary = "钉钉导入用户", description = "钉钉导入用户")
    @SaCheckPermission("system:user:dingding")
    @PostMapping("/dingding")
    public void addUserByDingding() {
        baseService.addUserByDingding();
    }

    @Operation(summary = "更新钉钉用户信息", description = "更新钉钉用户信息")
    @SaCheckPermission("system:user:dingding")
    @PostMapping("/updateDingDing")
    public void updateUserByDingding() {
        baseService.updateUserByDingding();
    }

    @Operation(summary = "修改积分", description = "修改用户积分")
    @PatchMapping("/points")
    public void changePoints(@RequestBody @Valid UserPointChangeReq req) {
        baseService.changePoints(req);
    }

    @Operation(summary = "修改推送消息设置", description = "修改用户是否推送钉钉消息设置")
    @Parameter(name = "id", description = "用户ID", example = "1", in = ParameterIn.PATH)
    @PatchMapping("/{id}/push-message")
    public void updatePushMessage(@RequestBody @Valid UserPushMessageUpdateReq req, @PathVariable Long id) {
        baseService.updatePushMessage(req, id);
    }

    @Operation(summary = "下载积分导入模板", description = "下载积分导入模板")
    @SaCheckPermission("biz:user:importPoints")
    @GetMapping(value = "/points/import/template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadPointsImportTemplate(HttpServletResponse response) throws IOException {
        baseService.downloadPointsImportTemplate(response);
    }

    @Operation(summary = "批量导入积分", description = "通过Excel批量导入用户积分")
    @SaCheckPermission("biz:user:importPoints")
    @PostMapping("/points/import")
    public UserPointImportResp importPoints(@RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file,
                                            @RequestParam("type") @NotNull(message = "积分类型不能为空") PointsTypeEnum type,
                                            @RequestParam(value = "refId", required = false) Long refId,
                                            @RequestParam(value = "remark", required = false) String remark) {

        UserPointImportReq req = new UserPointImportReq();
        req.setFile(file);
        req.setType(type);
        req.setRefId(refId);
        req.setRemark(remark);

        return baseService.importPoints(req);
    }

    @SaIgnore
    @Operation(summary = "导出数据", description = "导出数据")
    @GetMapping("/userList")
    public List<UserDO> getUserList() {
        return baseService.list();
    }

}
