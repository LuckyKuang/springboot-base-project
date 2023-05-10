/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-- ----------------------------
-- Table structure for ky_permission
-- ----------------------------
DROP TABLE IF EXISTS `ky_permission`;
CREATE TABLE `ky_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_by` bigint NOT NULL,
  `create_time` datetime(3) NOT NULL,
  `update_by` bigint NOT NULL,
  `update_time` datetime(3) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `permission_name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_role
-- ----------------------------
DROP TABLE IF EXISTS `ky_role`;
CREATE TABLE `ky_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_by` bigint NOT NULL,
  `create_time` datetime(3) NOT NULL,
  `update_by` bigint NOT NULL,
  `update_time` datetime(3) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `role_name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `ky_role_permissions`;
CREATE TABLE `ky_role_permissions` (
  `role_id` bigint NOT NULL,
  `permissions_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permissions_id`),
  KEY `fk_ky_role_permissions_id` (`permissions_id`),
  CONSTRAINT `fk_ky_role_permissions_role_id` FOREIGN KEY (`role_id`) REFERENCES `ky_role` (`id`),
  CONSTRAINT `fk_ky_role_permissions_id` FOREIGN KEY (`permissions_id`) REFERENCES `ky_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_user
-- ----------------------------
DROP TABLE IF EXISTS `ky_user`;
CREATE TABLE `ky_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_by` bigint NOT NULL,
  `create_time` datetime(3) NOT NULL,
  `update_by` bigint NOT NULL,
  `update_time` datetime(3) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `phone` varchar(32) NOT NULL,
  `user_status` varchar(32) NOT NULL,
  `username` varchar(128) NOT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ky_user_phone_uk` (`phone`),
  KEY `fk_ky_user_role_id` (`role_id`),
  CONSTRAINT `fk_ky_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `ky_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;