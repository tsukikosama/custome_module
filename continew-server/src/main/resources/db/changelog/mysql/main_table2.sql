-- liquibase formatted sql

-- changeset weilai:10008
-- comment 用户表新增钉钉id字段
ALTER TABLE `sys_user`
    ADD COLUMN `dingding_id` varchar(50) NULL DEFAULT NULL COMMENT '钉钉的id' AFTER `tenant_id`;

-- changeset weilai:10010
-- comment 新增unionid
ALTER TABLE `sys_user`
    ADD COLUMN `union_id` varchar(50) NULL COMMENT '员工在当前开发者企业账号范围内的唯一标识。' AFTER `dingding_id`;

-- changeset weilai:10027
-- comment 新增职位 和 入职日期
ALTER TABLE `sys_user`
    ADD COLUMN `job_title` varchar(64) NULL COMMENT '职位' AFTER `union_id`,
    ADD COLUMN `hired_date` datetime NULL COMMENT '入职时间' AFTER `job_title`;

-- changeset weilai:10028
-- comment 新增生日
ALTER TABLE `sys_user`
    ADD COLUMN `birthday` datetime NULL COMMENT '生日' AFTER `union_id`;

-- changeset weilai:10031
-- comment 新增配置是否推送钉钉消息的开关
ALTER TABLE `sys_user`
    ADD COLUMN `is_push_message` bit(1) NULL DEFAULT b'0' COMMENT '是否推送数据' AFTER `hired_date`;