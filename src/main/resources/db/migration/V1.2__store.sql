/*
Navicat MySQL Data Transfer

Source Server         : ly
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : store

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-05-11 09:34:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `flyway_schema_history`
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES ('1', '1.1', 'store', 'SQL', 'V1.1__store.sql', '98137852', 'root', '2020-04-04 16:02:06', '582', '1');

-- ----------------------------
-- Table structure for `s_category`
-- ----------------------------
DROP TABLE IF EXISTS `s_category`;
CREATE TABLE `s_category` (
  `id` int(2) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `category_name` varchar(50) DEFAULT NULL COMMENT '分类名称',
  `parent_id` int(2) DEFAULT NULL COMMENT '分类父级id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `operation` varchar(50) DEFAULT NULL COMMENT '操作人',
  `version` int(2) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_category
-- ----------------------------
INSERT INTO `s_category` VALUES ('1', '服装', '0', '2020-04-08 21:46:54', '2020-04-08 21:46:54', '0', '管理员', '0');
INSERT INTO `s_category` VALUES ('2', '衣服', '1', '2020-04-08 21:46:59', '2020-04-11 22:05:40', '0', '管理员', '18');
INSERT INTO `s_category` VALUES ('3', '鞋子', '1', '2020-04-08 21:47:47', '2020-04-08 21:47:50', '0', '管理员', '0');
INSERT INTO `s_category` VALUES ('4', '毛衣', '2', '2020-04-08 21:48:14', '2020-04-11 22:21:59', '0', '管理员', '12');
INSERT INTO `s_category` VALUES ('5', '皮鞋', '3', '2020-04-08 21:48:48', '2020-04-08 21:48:53', '0', '管理员', '0');
INSERT INTO `s_category` VALUES ('6', '夹克', '2', '2020-04-08 21:50:04', '2020-04-08 21:50:08', '0', '管理员', '0');

-- ----------------------------
-- Table structure for `s_custom`
-- ----------------------------
DROP TABLE IF EXISTS `s_custom`;
CREATE TABLE `s_custom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cus_phone` varchar(11) DEFAULT NULL COMMENT '客户电话',
  `cus_name` varchar(11) DEFAULT NULL COMMENT '客户名称',
  `cus_discount` int(3) DEFAULT NULL COMMENT '折扣',
  `cus_members` int(1) DEFAULT NULL COMMENT '等级',
  `cus_address` varchar(11) DEFAULT NULL COMMENT '用户地址',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `version` int(1) DEFAULT '1' COMMENT '用户版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_custom
-- ----------------------------
INSERT INTO `s_custom` VALUES ('1', '2020-05-11 09:34:08', '2020-05-11 09:34:08', '13769781954', '赵六', '9', '5', '云南省', '0', '4');

-- ----------------------------
-- Table structure for `s_order_item`
-- ----------------------------
DROP TABLE IF EXISTS `s_order_item`;
CREATE TABLE `s_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单项id',
  `order_item_no` varchar(255) NOT NULL COMMENT '订单项号',
  `order_main_id` bigint(20) NOT NULL COMMENT '主订单编号',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `goods_price` bigint(20) DEFAULT NULL COMMENT '商品单价',
  `goods_num` int(11) DEFAULT NULL COMMENT '预定数量',
  `order_item_status` int(11) DEFAULT NULL COMMENT '子订单状态',
  `goods_total` bigint(20) DEFAULT NULL COMMENT '子订单支付总金额',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_order_item
-- ----------------------------
INSERT INTO `s_order_item` VALUES ('13', '1259347289177067520', '31', '女神皮鞋', '2', '300', '2', '8', '54000', '0', '0', '2020-05-10 12:59:26', '2020-05-10 12:59:26');

-- ----------------------------
-- Table structure for `s_order_main`
-- ----------------------------
DROP TABLE IF EXISTS `s_order_main`;
CREATE TABLE `s_order_main` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` varchar(255) NOT NULL COMMENT '订单编号',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_amount` bigint(20) NOT NULL COMMENT '订单支付总金额',
  `user_phone` varchar(255) DEFAULT NULL COMMENT '买家联系电话',
  `pay_type` int(11) DEFAULT NULL COMMENT '支付方式',
  `pay_status` int(11) DEFAULT NULL COMMENT '支付状态',
  `operation` varchar(255) DEFAULT NULL COMMENT '下单人',
  `order_address` varchar(255) DEFAULT NULL COMMENT '用户收货地址',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_order_main
-- ----------------------------
INSERT INTO `s_order_main` VALUES ('31', '1259347288757637120', '8', '54000', '13769781954', '3', '1', null, '云南曲靖', '0', '0', '2020-05-10 12:59:26', '2020-05-10 12:59:26');

-- ----------------------------
-- Table structure for `s_store`
-- ----------------------------
DROP TABLE IF EXISTS `s_store`;
CREATE TABLE `s_store` (
  `id` int(2) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `parent_name` varchar(255) DEFAULT '' COMMENT '所属一级分类名称',
  `children_name` varchar(255) DEFAULT '' COMMENT '所属二级分类名称',
  `store_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `store_num` int(10) unsigned DEFAULT '0' COMMENT '商品库存',
  `store_status` int(1) DEFAULT '1' COMMENT '商品状态',
  `store_fee` bigint(10) unsigned DEFAULT '0' COMMENT '商品单价(单位:分)',
  `store_param` varchar(255) DEFAULT NULL COMMENT '商品属性',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `operation` varchar(255) DEFAULT NULL COMMENT '操作人',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_store
-- ----------------------------
INSERT INTO `s_store` VALUES ('1', '2020-05-10 12:16:12', '2020-05-10 12:16:13', '衣服', '毛衣', '虎皮毛衣', '0', '1', '10000', '尺码:43,颜色:白色', '0', '13769781955', '1');
INSERT INTO `s_store` VALUES ('2', '2020-05-10 12:59:25', '2020-05-10 12:59:26', '鞋子', '皮鞋', '女神皮鞋', '5', '1', '30000', '尺码:43,颜色:粉色', '0', '管理员', '0');
INSERT INTO `s_store` VALUES ('9', '2020-05-10 10:28:14', '2020-05-10 10:28:14', '鞋子', '皮鞋', '大皮鞋', '10', '2', '1100', '尺码:44,颜色:黑色', '0', '13769781955', '0');

-- ----------------------------
-- Table structure for `s_sysuser`
-- ----------------------------
DROP TABLE IF EXISTS `s_sysuser`;
CREATE TABLE `s_sysuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `user_phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `password` varchar(100) DEFAULT NULL COMMENT '用户登录密码',
  `user_age` int(1) DEFAULT NULL COMMENT '用户年龄',
  `user_sex` varchar(10) DEFAULT NULL COMMENT '用户性别',
  `user_address` varchar(50) DEFAULT NULL COMMENT '用户家庭住址',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `admin_flag` varchar(20) DEFAULT NULL COMMENT '角色',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除字段1删除0正常',
  `version` int(11) DEFAULT '0' COMMENT '用户版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_sysuser
-- ----------------------------
INSERT INTO `s_sysuser` VALUES ('98', '李毅', '15102270077', '$2a$10$wltToayFesontjiZi8KIG.Y/lRrZ0A47k9w.EfjgkZtOfYp0sLn6S', '22', '女', '云南省曲靖市沾益区', '2020-05-09 07:45:30', '2020-05-09 07:45:30', '员工管理,库存管理,订单管理,客户管理', '0', '2');
INSERT INTO `s_sysuser` VALUES ('99', '13769781932', '13769781954', '$2a$10$3H3sXR4Io10D6T0RC44IluR8GtuCLOOgxJM8PGqbmpask6OxC.Wpu', '21', '男', '云南省曲靖市马龙区2', '2020-05-02 13:24:14', '2020-05-02 13:24:14', '订单管理', '0', '1');