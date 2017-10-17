-- ----------------------------
-- Table structure for t_sms_mode
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_mode`;
CREATE TABLE `t_sms_mode` (
  `mod_id` int(32) NOT NULL COMMENT '模板id',
  `mod_comp_id` int(32) DEFAULT NULL COMMENT '商户id',
  `mod_num_id` int(32) DEFAULT NULL COMMENT '下行号码',
  `mod_content` varchar(256) DEFAULT NULL COMMENT '模板内容',
  `mod_reg` varchar(256) DEFAULT NULL COMMENT '模板正则表达式',
  `mod_reg_cfg` varchar(256) DEFAULT NULL COMMENT '正则表达式配置',
  `mod_reg_group` int(11) DEFAULT NULL COMMENT '正则组数',
  `mod_status` int(11) DEFAULT NULL COMMENT '模板状态',
  `mod_check_id` int(32) DEFAULT NULL COMMENT '审核人id',
  `mod_creattime` timestamp NULL DEFAULT NULL COMMENT '创见时间',
  `mod_checktime` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `mod_reason` varchar(256) DEFAULT '' COMMENT '审核不通过原因'
) CHARSET=utf8 COMMENT='模板表';
