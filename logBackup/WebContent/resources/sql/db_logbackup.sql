/*
Navicat MySQL Data Transfer

Source Server         : porterrecord
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : logbackup

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2018-07-04 01:54:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL,
  `exec_date` timestamp NULL DEFAULT NULL,
  `task_name` varchar(6) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10858 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES ('10826', '82', '2018-05-15 15:30:51', 'backup');
INSERT INTO `record` VALUES ('10827', '82', '2018-05-15 15:32:24', 'upload');
INSERT INTO `record` VALUES ('10828', '82', '2018-05-15 15:32:28', 'clean');
INSERT INTO `record` VALUES ('10829', '83', '2018-05-15 15:32:36', 'backup');
INSERT INTO `record` VALUES ('10830', '82', '2018-05-15 15:33:12', 'backup');
INSERT INTO `record` VALUES ('10831', '83', '2018-05-15 15:50:10', 'upload');
INSERT INTO `record` VALUES ('10832', '83', '2018-05-15 15:50:13', 'clean');
INSERT INTO `record` VALUES ('10833', '83', '2018-05-15 15:50:16', 'backup');
INSERT INTO `record` VALUES ('10834', '84', '2018-05-15 15:50:20', 'backup');
INSERT INTO `record` VALUES ('10835', '85', '2018-05-15 15:50:23', 'backup');
INSERT INTO `record` VALUES ('10836', '86', '2018-05-15 15:50:27', 'backup');
INSERT INTO `record` VALUES ('10837', '87', '2018-05-15 15:50:30', 'backup');
INSERT INTO `record` VALUES ('10838', '88', '2018-05-15 15:52:52', 'backup');
INSERT INTO `record` VALUES ('10839', '89', '2018-05-15 15:52:56', 'backup');
INSERT INTO `record` VALUES ('10840', '89', '2018-05-15 15:53:06', 'backup');
INSERT INTO `record` VALUES ('10841', '89', '2018-05-15 15:53:07', 'backup');
INSERT INTO `record` VALUES ('10842', '89', '2018-05-15 15:53:08', 'backup');
INSERT INTO `record` VALUES ('10843', '89', '2018-05-15 15:53:09', 'backup');
INSERT INTO `record` VALUES ('10844', '89', '2018-05-15 15:53:10', 'backup');
INSERT INTO `record` VALUES ('10845', '89', '2018-05-15 15:53:10', 'backup');
INSERT INTO `record` VALUES ('10846', '89', '2018-05-15 15:53:11', 'backup');
INSERT INTO `record` VALUES ('10847', '90', '2018-05-15 16:08:08', 'backup');
INSERT INTO `record` VALUES ('10848', '90', '2018-05-15 16:08:11', 'backup');
INSERT INTO `record` VALUES ('10849', '90', '2018-05-15 16:08:12', 'backup');
INSERT INTO `record` VALUES ('10850', '90', '2018-05-15 16:08:12', 'backup');
INSERT INTO `record` VALUES ('10851', '90', '2018-05-15 16:08:12', 'backup');
INSERT INTO `record` VALUES ('10852', '90', '2018-05-15 16:08:13', 'backup');
INSERT INTO `record` VALUES ('10853', '90', '2018-05-15 16:08:13', 'backup');
INSERT INTO `record` VALUES ('10854', '90', '2018-05-15 16:08:13', 'backup');
INSERT INTO `record` VALUES ('10855', '90', '2018-05-15 16:08:13', 'backup');
INSERT INTO `record` VALUES ('10856', '90', '2018-05-15 16:08:14', 'backup');
INSERT INTO `record` VALUES ('10857', '90', '2018-05-15 16:08:14', 'backup');

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(50) NOT NULL,
  `ip_addr` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of service
-- ----------------------------
INSERT INTO `service` VALUES ('83', 'service2', '0:0:0:0:0:0:0:1');
INSERT INTO `service` VALUES ('89', 'service8', '0:0:0:0:0:0:0:1');
INSERT INTO `service` VALUES ('90', 'service1', '0:0:0:0:0:0:0:1');
