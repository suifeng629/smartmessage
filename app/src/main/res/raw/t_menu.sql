-- ----------------------------
-- Table structure for t_sdk_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sdk_menu`;
CREATE TABLE `t_sdk_menu` (
  `menu_id` int(32) NOT NULL COMMENT '菜单id',
  `menu_num_id` int(32) DEFAULT NULL COMMENT '下行号码',
  `menu_level` int(32) DEFAULT NULL COMMENT '菜单级别',
  `menu_sort` int(32) DEFAULT NULL COMMENT '菜单排序值',
  `menu_parent_id` int(32) DEFAULT NULL COMMENT '父菜单id',
  `menu_url` varchar(256) DEFAULT NULL COMMENT '菜单url地址',
  `menu_status` int(11) DEFAULT NULL COMMENT '菜单状态',
  `menu_name` varchar(256) DEFAULT '' COMMENT '菜单名称',
  `menu_com_id` int(32) DEFAULT NULL COMMENT '商户id',
  `menu_check_id` int(32) DEFAULT NULL COMMENT '审核人id',
  `menu_inserttime` timestamp NULL DEFAULT NULL COMMENT '申请时间',
  `menu_opt_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `menu_reason` varchar(256) DEFAULT '' COMMENT '审核不通过原因',
  PRIMARY KEY (`menu_id`)
) CHARSET=utf8 COMMENT='短信详情页菜单，根据不同的号码显示不同的菜单';