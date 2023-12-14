/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : jsp_blog

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 10/12/2023 13:47:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `typeID` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `phTime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `number` int DEFAULT NULL,
  `image` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `voteNumber` int DEFAULT '0',
  `status` int DEFAULT '1',
  `userId` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
BEGIN;
INSERT INTO `tb_article` VALUES (25, '9', 'java程序设计', '<p>测试1<span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span><span style=\"font-size: 1em;\">测试1</span></p>', '2023年11月27日 星期一', 33, 'https://img1.baidu.com/it/u=4049022245,514596079&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1701190800&t=cfbdd66bb2fbaabb964e67e0a21d9c91', 1, 1, 1);
INSERT INTO `tb_article` VALUES (26, '9', 'C语言程序设计', '<p>测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2测试2</p>', '2023年11月27日 星期一', 6, 'https://img0.baidu.com/it/u=530426417,2082848644&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1701190800&t=2ca2aa52cda10198062e4c1dd5ad0afd', 0, 1, 1);
INSERT INTO `tb_article` VALUES (27, '10', 'JSP', '<p>JSP<span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span><span style=\"font-size: 1em;\">JSP</span></p>', '2023年11月27日 星期一', 1, 'https://img1.baidu.com/it/u=3712997108,442011921&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1701190800&t=a26fcd1bfab23e4ec67aa28eefc200bd', 0, 1, 1);
INSERT INTO `tb_article` VALUES (28, '12', '软件分享', '<p>软件分享<span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span><span style=\"font-size: 1em;\">软件分享</span></p>', '2023年11月27日 星期一', 2, 'https://img1.baidu.com/it/u=2080801041,3349735074&fm=253&fmt=auto&app=120&f=JPEG?w=1139&h=640', 0, 1, 36);
COMMIT;

-- ----------------------------
-- Table structure for tb_articletype
-- ----------------------------
DROP TABLE IF EXISTS `tb_articletype`;
CREATE TABLE `tb_articletype` (
  `id` int NOT NULL AUTO_INCREMENT,
  `typeName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_articletype
-- ----------------------------
BEGIN;
INSERT INTO `tb_articletype` VALUES (9, '个人笔记', '个人笔记', '2');
INSERT INTO `tb_articletype` VALUES (10, '日常随笔', '日常随笔', '1');
INSERT INTO `tb_articletype` VALUES (11, '项目分享', '项目分享', '2');
INSERT INTO `tb_articletype` VALUES (12, '奇技淫巧', '奇技淫巧', '1');
INSERT INTO `tb_articletype` VALUES (13, '碎言碎语', '碎言碎语', NULL);
COMMIT;

-- ----------------------------
-- Table structure for tb_consumer
-- ----------------------------
DROP TABLE IF EXISTS `tb_consumer`;
CREATE TABLE `tb_consumer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_consumer
-- ----------------------------
BEGIN;
INSERT INTO `tb_consumer` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '1', '2023-12-10 12:13:58', 'admin@qq.com');
INSERT INTO `tb_consumer` VALUES (36, 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'user', '2', '2023-12-10 12:14:01', 'user@qq.com');
COMMIT;

-- ----------------------------
-- Table structure for tb_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_data`;
CREATE TABLE `tb_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `aId` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of tb_data
-- ----------------------------
BEGIN;
INSERT INTO `tb_data` VALUES (17, 1, 25, 1);
INSERT INTO `tb_data` VALUES (23, 1, 26, 1);
INSERT INTO `tb_data` VALUES (24, 1, 27, 1);
INSERT INTO `tb_data` VALUES (26, 1, 25, 2);
COMMIT;

-- ----------------------------
-- Table structure for tb_vote
-- ----------------------------
DROP TABLE IF EXISTS `tb_vote`;
CREATE TABLE `tb_vote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `voteName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `voteTime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `voteContent` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `voteLink` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_vote
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
