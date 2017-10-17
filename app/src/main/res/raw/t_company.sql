-- ----------------------------
-- Table structure for t_number_company
-- ----------------------------
DROP TABLE IF EXISTS `t_number_company`;
CREATE TABLE `t_number_company` (
  `num_id` int(32) NOT NULL COMMENT '号码id',
  `num_comp_id` int(32) DEFAULT NULL COMMENT '商户id',
  `num_phone` char(32) DEFAULT '' COMMENT '下行号码',
  `num_title` varchar(256) DEFAULT NULL COMMENT '短信详情页标题',
  `num_logo_url` varchar(256) DEFAULT NULL COMMENT '下行号码对应logo',
  `num_status` timestamp NULL DEFAULT NULL COMMENT '状态',
  `num_check_id` int(11) DEFAULT NULL COMMENT '审核人id',
  `num_inserttime` timestamp NULL DEFAULT NULL COMMENT '申请时间',
  `num_checktime` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `num_newtime` timestamp NULL DEFAULT NULL COMMENT '缓存刷新时间',
  `num_reason` varchar(256) DEFAULT '' COMMENT '审核不通过原因',
  PRIMARY KEY (`num_id`)
) CHARSET=utf8 COMMENT='下行号码与公司对应关系表';
