-- ----------------------------
-- Table structure for `s_sysuser`
-- ----------------------------
DROP TABLE IF EXISTS `s_sysuser`;
CREATE TABLE `s_sysuser` (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `user_phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `password` varchar(100) DEFAULT NULL COMMENT '用户登录密码',
  `user_age` int(1) DEFAULT NULL COMMENT '用户年龄',
  `user_sex` int(1) DEFAULT NULL COMMENT '用户性别1男性，2女性',
  `user_address` varchar(50) DEFAULT NULL COMMENT '用户家庭住址',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `admin_flag` int(1) DEFAULT NULL COMMENT '管理员标志1是，0不是',
  `deleted` int(1) DEFAULT NULL COMMENT '逻辑删除字段1删除0正常',
  `version` int(11) DEFAULT NULL COMMENT '用户版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_sysuser
-- ----------------------------
INSERT INTO `s_sysuser` VALUES ('1244232268453539842', 'ls', '123', '$2a$10$h47ourI0OIxZHxEtwa04VOx1D.RTK4BJf5OVN8lWrTPghJjizjJ0i', '123', null, '1111', '2020-03-29 19:57:44', '2020-03-29 19:57:44', '1', '1', null);