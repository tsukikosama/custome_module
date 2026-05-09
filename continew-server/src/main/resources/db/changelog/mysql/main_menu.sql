-- liquibase formatted sql

-- changeset weilai:1
-- comment 菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (801878128262647954, '商品管理', 0, 1, '/product', 'Product', 'Layout', '/product/index', 'apps', b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-14 18:17:24', 1, '2026-01-14 18:24:41', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (801878269493252245, '商品列表', 801878128262647954, 2, '/product/index', 'ProductIndex', 'biz/product/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-14 18:17:58', 1, '2026-01-14 18:23:34', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363969, '列表', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:list', 1, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363970, '详情', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:get', 2, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363971, '新增', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:create', 3, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363972, '修改', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:update', 4, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363973, '删除', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:delete', 5, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2011377905089363974, '导出', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:export', 6, 1, 1, '2026-01-14 18:19:44', NULL, NULL, 0);

-- changeset weilai:2
-- comment 商品类型菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802125127645925423, '商品分类', 801878128262647954, 2, '/biz/product/type', 'BizProductType', 'biz/productType/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-15 10:38:53', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2011627415979888641, '列表', 802125127645925423, 3, 'biz:productType:list', 1, 1, 1, NOW()),
    (2011627415979888642, '详情', 802125127645925423, 3, 'biz:productType:get', 2, 1, 1, NOW()),
    (2011627415979888643, '新增', 802125127645925423, 3, 'biz:productType:create', 3, 1, 1, NOW()),
    (2011627415979888644, '修改', 802125127645925423, 3, 'biz:productType:update', 4, 1, 1, NOW()),
    (2011627415979888645, '删除', 802125127645925423, 3, 'biz:productType:delete', 5, 1, 1, NOW()),
    (2011627415979888646, '导出', 802125127645925423, 3, 'biz:productType:export', 6, 1, 1, NOW());

-- changeset weilai:3
-- comment 新增商品库存表
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802143808153194517, '商品库存', 801878128262647954, 2, '/biz/productStock', 'BizProductStock', 'biz/productStock/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-15 11:53:07', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2011645266233102337, '列表', 802143808153194517, 3, 'biz:productStock:list', 1, 1, 1, NOW()),
    (2011645266233102338, '详情', 802143808153194517, 3, 'biz:productStock:get', 2, 1, 1, NOW()),
    (2011645266233102339, '新增', 802143808153194517, 3, 'biz:productStock:create', 3, 1, 1, NOW()),
    (2011645266233102340, '修改', 802143808153194517, 3, 'biz:productStock:update', 4, 1, 1, NOW()),
    (2011645266233102341, '删除', 802143808153194517, 3, 'biz:productStock:delete', 5, 1, 1, NOW()),
    (2011645266233102342, '导出', 802143808153194517, 3, 'biz:productStock:export', 6, 1, 1, NOW());

-- changeset weilai:4
-- comment 商品状态日志菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802203201418825781, '订单日志', 801878128262647954, 2, '/biz/orderLog', 'OrderLog', 'biz/productOrderLog/index', '', NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-15 15:49:07', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2011705269002403841, '列表', 802203201418825781, 3, 'biz:productOrderLog:list', 1, 1, 1, NOW()),
    (2011705269002403842, '详情', 802203201418825781, 3, 'biz:productOrderLog:get', 2, 1, 1, NOW()),
    (2011705269002403843, '新增', 802203201418825781, 3, 'biz:productOrderLog:create', 3, 1, 1, NOW()),
    (2011705269002403844, '修改', 802203201418825781, 3, 'biz:productOrderLog:update', 4, 1, 1, NOW()),
    (2011705269002403845, '删除', 802203201418825781, 3, 'biz:productOrderLog:delete', 5, 1, 1, NOW()),
    (2011705269002403846, '导出', 802203201418825781, 3, 'biz:productOrderLog:export', 6, 1, 1, NOW());

-- changeset weilai:5
-- comment 新增订单菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802249699250671688, '订单列表', 801878128262647954, 2, '/biz/order', 'BizOrder', 'biz/order/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-15 18:53:53', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2011711343306702849, '列表', 802249699250671688, 3, 'biz:order:list', 1, 1, 1, NOW()),
    (2011711343306702850, '详情', 802249699250671688, 3, 'biz:order:get', 2, 1, 1, NOW()),
    (2011711343306702851, '新增', 802249699250671688, 3, 'biz:order:create', 3, 1, 1, NOW()),
    (2011711343306702852, '修改', 802249699250671688, 3, 'biz:order:update', 4, 1, 1, NOW()),
    (2011711343306702853, '删除', 802249699250671688, 3, 'biz:order:delete', 5, 1, 1, NOW()),
    (2011711343306702854, '导出', 802249699250671688, 3, 'biz:order:export', 6, 1, 1, NOW());

-- changeset weilai:6
-- comment 新增积分日志菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802544320199532573, '积分管理', 0, 1, '/points', 'Points', 'Layout', NULL, 'storage', b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-16 14:24:36', 1, '2026-01-16 14:24:58', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (802544578761596963, '积分日志', 802544320199532573, 2, '/biz/points/log', 'BizPointsLog', 'biz/pointsLog/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-16 14:25:38', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2012046715110776833, '列表', 802544578761596963, 3, 'biz:pointsLog:list', 1, 1, 1, NOW()),
    (2012046715110776834, '详情', 802544578761596963, 3, 'biz:pointsLog:get', 2, 1, 1, NOW()),
    (2012046715110776835, '新增', 802544578761596963, 3, 'biz:pointsLog:create', 3, 1, 1, NOW()),
    (2012046715110776836, '修改', 802544578761596963, 3, 'biz:pointsLog:update', 4, 1, 1, NOW()),
    (2012046715110776837, '删除', 802544578761596963, 3, 'biz:pointsLog:delete', 5, 1, 1, NOW()),
    (2012046715110776838, '导出', 802544578761596963, 3, 'biz:pointsLog:export', 6, 1, 1, NOW());

-- changeset weilai:7
-- comment 新增加班菜单
-- 加班记录管理按钮
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (803638904879058970, '加班记录', 802544320199532573, 2, '/overtimeWork', 'OvertimeWork', 'overtimeWork/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-19 14:54:06', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2013141899856773121, '列表', 803638904879058970, 3, 'biz:overtimeWork:list', 1, 1, 1, NOW()),
    (2013141899856773122, '详情', 803638904879058970, 3, 'biz:overtimeWork:get', 2, 1, 1, NOW()),
    (2013141899856773123, '新增', 803638904879058970, 3, 'biz:overtimeWork:create', 3, 1, 1, NOW()),
    (2013141899856773124, '修改', 803638904879058970, 3, 'biz:overtimeWork:update', 4, 1, 1, NOW()),
    (2013141899856773125, '删除', 803638904879058970, 3, 'biz:overtimeWork:delete', 5, 1, 1, NOW()),
    (2013141899856773126, '导出', 803638904879058970, 3, 'biz:overtimeWork:export', 6, 1, 1, NOW());

-- changeset weilai:8
-- comment 新增钉钉事件日志表菜单
-- 钉钉事件日志表管理按钮
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (803591946898509853, '钉钉事件日志', 2000, 2, '/stream/event', 'StreamEvent', 'biz/dingdingStreamEvent/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-19 11:47:30', NULL, NULL, 0);
INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2013094717019578369, '列表', 803591946898509853, 3, 'biz:dingdingStreamEvent:list', 1, 1, 1, NOW()),
    (2013094717019578370, '详情', 803591946898509853, 3, 'biz:dingdingStreamEvent:get', 2, 1, 1, NOW()),
    (2013094717019578371, '新增', 803591946898509853, 3, 'biz:dingdingStreamEvent:create', 3, 1, 1, NOW()),
    (2013094717019578372, '修改', 803591946898509853, 3, 'biz:dingdingStreamEvent:update', 4, 1, 1, NOW()),
    (2013094717019578373, '删除', 803591946898509853, 3, 'biz:dingdingStreamEvent:delete', 5, 1, 1, NOW()),
    (2013094717019578374, '导出', 803591946898509853, 3, 'biz:dingdingStreamEvent:export', 6, 1, 1, NOW());

-- changeset weilai:9
-- comment 新增心愿表管理菜单
-- 心愿表管理菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804039668390699030, '心愿管理', 0, 1, '/wish', 'Wish', 'Layout', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-20 17:26:35', 1, '2026-01-20 17:27:32', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804040060381962269, '心愿列表', 804039668390699030, 2, '/wish/index', 'WishIndex', 'biz/wish/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-01-20 17:28:09', NULL, NULL, 0);

INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
    (2013542816749920257, '列表', 804040060381962269, 3, 'biz:wish:list', 1, 1, 1, NOW()),
    (2013542816749920258, '详情', 804040060381962269, 3, 'biz:wish:get', 2, 1, 1, NOW()),
    (2013542816749920259, '新增', 804040060381962269, 3, 'biz:wish:create', 3, 1, 1, NOW()),
    (2013542816749920260, '修改', 804040060381962269, 3, 'biz:wish:update', 4, 1, 1, NOW()),
    (2013542816749920261, '删除', 804040060381962269, 3, 'biz:wish:delete', 5, 1, 1, NOW()),
    (2013542816749920262, '导出', 804040060381962269, 3, 'biz:wish:export', 6, 1, 1, NOW());

-- changeset weilai:10
-- comment 新增菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804667780841357377, '积分变更', 802544320199532573, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:points:add', 999, 1, 1, '2026-01-22 11:02:29', 1, '2026-01-22 11:02:56', 804667780841357377);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804681491995967545, '上/下架', 801878269493252245, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:product:disable', 999, 1, 1, '2026-01-22 11:56:58', 1, '2026-01-22 11:57:37', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804683162654359676, '转换成商品', 804040060381962269, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:wish:toProduct', 999, 1, 1, '2026-01-22 12:03:36', 1, '2026-01-22 13:38:26', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804707595095916688, '钉钉日志', 803591946898509853, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:dingdingStreamEvent:page', 999, 1, 1, '2026-01-22 13:40:41', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804721223660486695, '下单', 802249699250671688, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:order:create', 999, 1, 1, '2026-01-22 14:34:51', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (804789162652746004, '取消下单', 802249699250671688, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:order:refund', 999, 1, 1, '2026-01-22 19:04:49', NULL, NULL, 0);

-- changeset weilai:11
-- comment 新增菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842354528777936920, '活动管理', 0, 1, '/activity', 'activity', 'Layout', NULL, 'highlight', b'0', b'0', b'0', NULL, 999, 1, 1, '2026-05-06 10:56:10', 1, '2026-05-06 11:11:13', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842357788863635535, '活动列表', 842354528777936920, 2, '/activity/index', 'ActivityIndex', 'biz/activity/index', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-05-06 11:09:07', 1, '2026-05-06 11:11:25', 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334401, '列表', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:list', 1, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334402, '详情', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:get', 2, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334403, '新增', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:create', 3, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334404, '修改', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:update', 4, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334405, '删除', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:delete', 5, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051856720479334406, '导出', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:export', 6, 1, 1, '2026-05-06 11:09:35', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842462962064039939, '审批', 842357788863635535, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:review', 999, 1, 1, '2026-05-06 18:07:02', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842463343062032393, '活动图片', 842354528777936920, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:activity:image', 999, 1, 1, '2026-05-06 18:08:33', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842465622834028575, '建议管理', 0, 1, '/sugestion', 'Sugestion', 'Layout', NULL, NULL, b'0', b'0', b'0', NULL, 999, 1, 1, '2026-05-06 18:17:37', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (842466088196251684, '建议列表', 842465622834028575, 2, '/suggestion/list', 'SuggestionList', 'biz/suggestion/index', NULL, 'attachment', b'0', b'0', b'0', NULL, 999, 1, 1, '2026-05-06 18:19:28', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488385, '列表', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:list', 1, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488386, '详情', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:get', 2, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488387, '新增', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:create', 3, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488388, '修改', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:update', 4, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488389, '删除', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:delete', 5, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2051968966825488390, '导出', 842466088196251684, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:suggestion:export', 6, 1, 1, '2026-05-06 18:20:05', NULL, NULL, 0);

-- changeset weilai:12
-- comment 新增菜单
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (843422737916534817, '轮播图管理', 1000, 2, '/carousel', 'Carousel', 'biz/carouselImage/index', NULL, 'palette', b'0', b'0', b'0', NULL, 999, 1, 1, '2026-05-09 09:40:51', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222977, '列表', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:list', 1, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222978, '详情', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:get', 2, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222979, '新增', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:create', 3, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222980, '修改', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:update', 4, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222981, '删除', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:delete', 5, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`) VALUES (2052925992836222982, '导出', 843422737916534817, 3, NULL, NULL, NULL, NULL, NULL, b'0', b'0', b'0', 'biz:carouselImage:export', 6, 1, 1, '2026-05-09 14:56:23', NULL, NULL, 0);
