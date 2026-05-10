-- liquibase formatted sql

-- changeset weilai:10000
-- comment 初始化数据库
create table biz_product
(
    id          bigint              not null
        primary key,
    name        varchar(50)         not null comment '商品名称',
    image       varchar(1024)       null comment '商品图片',
    points      int                 not null comment '商品积分',
    type_id     bigint              not null comment '商品类型ID',
    description text                null comment '商品描述',
    tenant_id   bigint default 0    not null comment '租户ID',
    create_time datetime            not null comment '创建时间',
    create_user bigint              not null comment '创建人',
    update_time datetime            null comment '更新时间',
    update_user bigint              null comment '更新人',
    deleted     bigint default 0    null comment '是否已删除（0：否；id：是）',
    is_shelf    bit    default b'1' not null comment '是否上架',
    month_limit int                 not null comment '月限数量'
) comment '商品表';

create index idx_points
    on biz_product (points);

create index idx_tenant
    on biz_product (tenant_id);

create index idx_type
    on biz_product (type_id);


-- changeset weilai:10001
-- comment 初始化数据库
create table biz_product_type
(
    id          bigint           not null
        primary key,
    name        varchar(50)      not null comment '商品类型名称',
    tenant_id   bigint default 0 not null comment '租户ID',
    create_time datetime         not null comment '创建时间',
    create_user bigint           not null comment '创建人'
)
    comment '商品分类表';

create index idx_tenant
    on biz_product_type (tenant_id);



-- changeset weilai:10002
-- comment 初始化数据库
create table biz_order
(
    id          bigint           not null
        primary key,
    order_no    varchar(64)      not null comment '订单号',
    product_id  bigint           not null comment '商品ID',
    product_num int              not null comment '商品数量',
    user_id     bigint           not null comment '下单用户ID',
    status      tinyint          not null comment '订单状态',
    order_time  datetime         not null comment '下单时间',
    tenant_id   bigint default 0 not null comment '租户ID',
    create_time datetime         not null comment '创建时间',
    create_user bigint           not null comment '创建人',
    update_time datetime         null comment '更新时间',
    update_user bigint           null comment '更新人',
    deleted     bigint default 0 null comment '是否已删除（0：否；id：是）'
)
    comment '订单表';

create index idx_tenant
    on biz_order (tenant_id);

create index idx_user_time
    on biz_order (user_id, order_time);

create index index_status
    on biz_order (status);



-- changeset weilai:10003
-- comment 初始化数据库
create table biz_product_order_log
(
    id           bigint           not null
        primary key,
    order_id     bigint           not null comment '订单ID',
    user_id      bigint           null comment '关联用户ID',
    status       tinyint          not null comment '订单状态',
    remark       varchar(255)     null comment '备注',
    tenant_id    bigint default 0 not null comment '租户ID',
    create_time  datetime         not null comment '创建时间',
    deleted      bigint default 0 null comment '是否已删除（0：否；id：是）',
    after_status tinyint          null comment '更新之前的状态',
    create_user  bigint           not null comment '创建人'
)
    comment '订单状态日志表';

create index idx_order
    on biz_product_order_log (order_id);

create index idx_tenant
    on biz_product_order_log (tenant_id);




-- changeset weilai:10004
-- comment 初始化数据库
create table biz_points_log
(
    id            bigint           not null
        primary key,
    user_id       bigint           not null comment '用户ID',
    type          tinyint          not null comment '类型 1增加 2扣除',
    points        int              not null comment '积分数量',
    before_points int              not null comment '改变前数量',
    after_points  int              not null comment '改变后数量',
    remark        varchar(255)     null comment '备注',
    tenant_id     bigint default 0 not null comment '租户ID',
    create_time   datetime         not null comment '创建时间',
    deleted       bigint default 0 null comment '是否已删除（0：否；id：是）',
    create_user   bigint           not null comment '创建人'
)
    comment '积分日志表';

create index idx_tenant
    on biz_points_log (tenant_id);

create index idx_user
    on biz_points_log (user_id);


-- changeset weilai:10005
-- comment 初始化数据库
create table biz_overtime_work
(
    id                  bigint           not null
        primary key,
    user_id             bigint           not null comment '用户ID',
    duration            decimal(5, 2)    not null comment '时长(小时)',
    convert_points      int              not null comment '转换积分数量',
    status              varchar(20)      not null comment '状态 RUNNING：审批中：TERMINATED：已撤销 COMPLETED：审批完成',
    tenant_id           bigint default 0 not null comment '租户ID',
    create_time         datetime         not null comment '创建时间',
    create_user         bigint           not null comment '创建人',
    update_time         datetime         null comment '更新时间',
    update_user         bigint           null comment '更新人',
    deleted             bigint default 0 null comment '是否已删除（0：否；id：是）',
    result              varchar(20)      null comment '审批结果(1 通过 0 驳回）',
    process_instance_id varchar(50)      null comment '事件id',
    start_time          datetime         not null comment '加班的开始时间',
    end_time            datetime         not null comment '加班的结束时间'
)
    comment '加班记录表';

create index idx_tenant
    on biz_overtime_work (tenant_id);

create index idx_user
    on biz_overtime_work (user_id);

-- changeset weilai:10006
-- comment 初始化数据库
create table biz_wish
(
    id           bigint              not null comment 'id'
        primary key,
    name         varchar(50)         not null comment '商品名称',
    product_id   bigint              null comment '转成商品的id',
    is_product   bit    default b'0' not null comment '是否转成商品',
    create_time  datetime            not null comment '创建时间',
    create_user  bigint              not null comment '创建人',
    tenant_id    bigint default 0    not null comment '租户ID',
    deleted      bigint default 0    null comment '是否已删除（0：否；id：是）',
    wish_count   int    default 1    not null comment '心愿次数',
    wish_user_id varchar(1024)       null comment '许愿的用户'
);

-- changeset weilai:10007
-- comment 初始化数据库
create table biz_dingding_stream_event
(
    id                  bigint           not null comment 'id'
        primary key,
    dingding_id         varchar(50)      not null comment '钉钉的id',
    type                varchar(50)      not null comment '钉钉时间的类型',
    time                bigint           not null comment '钉钉的时间挫',
    content             text             not null comment '钉钉的数据',
    process_instance_id varchar(50)      null comment '事件id',
    tenant_id           bigint default 0 not null comment '租户ID',
    create_time         datetime         not null comment '创建时间'
);


-- changeset weilai:10008
-- comment 用户表新增积分字段
ALTER TABLE `sys_user`
    ADD COLUMN `points` bigint NOT NULL DEFAULT 0 COMMENT '积分' AFTER `tenant_id`;

-- changeset weilai:10009
-- comment 用户表新增钉钉id字段
ALTER TABLE `sys_user`
    ADD COLUMN `dingding_id` varchar(50) NULL DEFAULT NULL COMMENT '钉钉的id' AFTER `points`;

-- changeset weilai:10010
-- comment 新增unionid
ALTER TABLE `sys_user`
    ADD COLUMN `union_id` varchar(50) NULL COMMENT '员工在当前开发者企业账号范围内的唯一标识。' AFTER `dingding_id`;

-- changeset weilai:10011
-- comment 调整钉钉事件为通用的事件表
ALTER TABLE `biz_dingding_stream_event`
    DROP COLUMN `tenant_id`,
    CHANGE COLUMN `dingding_id` `event_id` varchar(50)  NOT NULL COMMENT '事件的id' AFTER `id`,
    MODIFY COLUMN `type` varchar(50)  NOT NULL COMMENT '事件类型' AFTER `event_id`,
    MODIFY COLUMN `time` bigint NOT NULL COMMENT '事件时间挫' AFTER `type`,
    MODIFY COLUMN `content` text  NOT NULL COMMENT '事件数据' AFTER `time`,
    MODIFY COLUMN `process_instance_id` varchar(50)  NULL COMMENT '流程id' AFTER `content`;
RENAME TABLE biz_dingding_stream_event TO biz_stream_event;

-- changeset weilai:10012
-- comment 调整积分订单表
ALTER TABLE `biz_order`
    DROP COLUMN `user_id`,
    DROP COLUMN `order_time`,
    DROP COLUMN `tenant_id`,
    ADD COLUMN `cost_points` decimal(10, 2) NOT NULL COMMENT '花费的积分' AFTER `status`;

-- changeset weilai:10013
-- comment 调整积分订单表
ALTER TABLE `biz_points_log`
    DROP COLUMN `tenant_id`,
    MODIFY COLUMN `type` tinyint NOT NULL COMMENT '来源类型  ' AFTER `user_id`,
    ADD COLUMN `ref_id` bigint NOT NULL COMMENT '来源id' AFTER `type`;

-- changeset weilai:10014
-- comment 调整商品表
ALTER TABLE `biz_product`
    DROP COLUMN `tenant_id`;
ALTER TABLE `biz_product_type`
    DROP COLUMN `tenant_id`;

-- changeset weilai:10015
-- comment 心愿表
ALTER TABLE `biz_wish`
    DROP COLUMN `tenant_id`,
    DROP COLUMN `wish_count`,
    DROP COLUMN `wish_user_id`,
    ADD COLUMN `parents_id` bigint NOT NULL DEFAULT 0 COMMENT '父id 如果为0 代表是父' AFTER `deleted`;

-- changeset weilai:10016
-- comment 订单表
ALTER TABLE `biz_order`
    ADD COLUMN `cancel_time` datetime NULL COMMENT '取消时间' AFTER `deleted`,
    ADD COLUMN `finish_time` datetime NULL COMMENT '完成时间' AFTER `cancel_time`,
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `finish_time`;

-- changeset weilai:10017
-- comment 积分日志
ALTER TABLE `biz_points_log`
    ADD COLUMN `status` tinyint NOT NULL COMMENT '失效状态 1-有效  0失效' AFTER `create_user`,
    ADD COLUMN `Invalid_time` datetime NULL COMMENT '失效时间' AFTER `status`;

-- changeset weilai:10018
-- comment 移除订单日志的关联用户id
ALTER TABLE `biz_product_order_log`
    DROP COLUMN `user_id`;

-- changeset weilai:10019
-- comment 许愿表优化
ALTER TABLE `biz_wish`
    ADD COLUMN `status` tinyint NOT NULL DEFAULT 1 COMMENT '许愿状态 1-心愿中 2-许愿成功 3-许愿失败  4-许愿取消' AFTER `parents_id`,
    ADD COLUMN `fail_reason` varchar(255) NULL COMMENT '失败原因' AFTER `status`;

-- changeset weilai:10020
-- comment 新增建议表
create table biz_suggestion
(
    id          bigint      not null comment 'id'
        primary key,
    tag         varchar(10) not null comment '标签',
    content     text        not null comment '内容',
    create_time datetime    not null comment '创建时间',
    create_user bigint      not null comment '创建人'
)comment '建议表';

-- changeset weilai:10021
-- comment 新增活动表
create table biz_activity
(
    id                   bigint               not null comment 'id'
        primary key,
    title                varchar(64)          not null comment '活动标题',
    image                varchar(128)         not null comment '首图',
    content              text                 not null comment '活动内容',
    type                 tinyint(1)           not null comment '活动类型（1：普通活动；2：分享会）',
    start_time           datetime             not null comment '活动开始时间',
    end_time             datetime             not null comment '活动结束时间',
    activity_people_nums int                  not null comment '活动人数（0代表不限制）',
    attachment           varchar(500)         null comment '附件URL（支持文档、PPT、PDF等）',
    status               tinyint default 1    not null comment '审批状态（0：审核失败；1：待审核；2：审核成功）',
    is_top               bit     default b'0' not null comment '是否置顶',
    audit_remark         varchar(255)         null comment '审核备注',
    audit_time           datetime             null comment '审核时间',
    audit_user           bigint               null comment '审核人ID',
    create_time          datetime             not null comment '创建时间',
    create_user          bigint               not null comment '创建人',
    update_time          datetime             null comment '更新时间',
    update_user          bigint               null comment '更新人',
    deleted              bigint  default 0    null comment '是否已删除（0：否；id：是）'
)
    comment '活动表';

create index idx_type_index
    on biz_activity (type);

create index index_start_time
    on biz_activity (start_time);

create index index_end_time
    on biz_activity (end_time);

create index index_status
    on biz_activity (status);

-- changeset weilai:10022
-- comment 新增活动成员
create table biz_activity_member
(
    id          bigint           not null comment 'id'
        primary key,
    activity_id bigint           not null comment '活动id',
    create_time datetime         not null comment '创建时间',
    create_user bigint           not null comment '创建人',
    deleted     bigint default 0 null comment '是否已删除（0：否；id：是）',
    user_id     bigint           not null comment '参加人员id',
    type        tinyint          not null comment '参加成员类型（1：演讲人；2：必参加人；3：主动参加）',
    status      tinyint          not null comment '状态（1：已报名；2：已取消）'
)
    comment '活动参与用户表';

create index index_activity_id
    on biz_activity_member (activity_id);

create index index_user_id
    on biz_activity_member (user_id);

-- changeset weilai:10023
-- comment 轮播图表
create table biz_carousel_image
(
    id          bigint       not null comment 'id'
        primary key,
    url         varchar(128) not null comment '图片地址',
    sort        tinyint(1)   not null comment '顺序',
    title       varchar(255) null comment '轮播图标题',
    jump_path   varchar(255) null comment '跳转路径',
    create_time datetime     not null comment '创建时间',
    create_user bigint       not null comment '创建人'
)comment '轮播图表';

-- changeset weilai:10024
-- comment 新增活动图片表
create table biz_activity_image
(
    id          bigint       not null comment 'id'
        primary key,
    activity_id bigint       not null comment '事件id',
    img_url     varchar(255) not null comment '图片路径',
    create_time datetime     not null comment '创建时间',
    create_user bigint       not null comment '创建人'
) comment '活动图片表';

-- changeset weilai:10025
-- comment 调整审批状态枚举描述
ALTER TABLE biz_activity
    MODIFY COLUMN status tinyint NOT NULL DEFAULT '1'
        COMMENT '审批状态（1：待审核；2：审核成功；3：审核失败）';

-- changeset weilai:10026
-- comment 轮播图新增内容
ALTER TABLE `biz_activity_image`
    ADD COLUMN `content` text NULL COMMENT '文章内容' AFTER `create_user`;

-- changeset weilai:10027
-- comment 新增职位 和 入职日期
ALTER TABLE `sys_user`
    ADD COLUMN `job_title` varchar(64) NULL COMMENT '职位' AFTER `union_id`,
    ADD COLUMN `hired_date` datetime NULL COMMENT '入职时间' AFTER `job_title`;

-- changeset weilai:10028
-- comment 新增生日
ALTER TABLE `sys_user`
    ADD COLUMN `birthday` datetime NULL COMMENT '生日' AFTER `union_id`;

-- changeset weilai:10029
-- comment 来源非必填
ALTER TABLE `biz_points_log`
    MODIFY COLUMN `ref_id` bigint NULL COMMENT '来源id' AFTER `type`;