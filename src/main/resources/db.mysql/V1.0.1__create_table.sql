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
-- Table structure for ky_dept
-- ----------------------------
DROP TABLE IF EXISTS `ky_dept`;
CREATE TABLE `ky_dept`  (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_by` int NOT NULL,
    `create_time` datetime(3) NULL,
    `update_by` int NOT NULL,
    `update_time` datetime(3) NULL,
    `name` varchar(128) NOT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    `sort` int NOT NULL,
    `status` tinyint(1) NOT NULL,
    `tree_path` varchar(8) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_menu
-- ----------------------------
DROP TABLE IF EXISTS `ky_menu`;
CREATE TABLE `ky_menu`  (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_by` int NOT NULL,
    `create_time` datetime(3) NULL,
    `update_by` int NOT NULL,
    `update_time` datetime(3) NULL,
    `component_path` varchar(128) DEFAULT NULL,
    `icon` varchar(128) DEFAULT NULL,
    `name` varchar(128) NOT NULL,
    `outer_link_url` varchar(255) DEFAULT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    `permission_name` varchar(128) DEFAULT NULL,
    `redirect_url` varchar(255) DEFAULT NULL,
    `router_path` varchar(128) DEFAULT NULL,
    `sort` int NOT NULL,
    `status` tinyint(1) NOT NULL,
    `type` tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_role
-- ----------------------------
DROP TABLE IF EXISTS `ky_role`;
CREATE TABLE `ky_role`  (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_by` int NOT NULL,
    `create_time` datetime(3) NULL,
    `update_by` int NOT NULL,
    `update_time` datetime(3) NULL,
    `code` varchar(128) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `name` varchar(128) NOT NULL,
    `sort` int NOT NULL,
    `status` tinyint(1) NOT NULL,
    `data_scope` tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_role_menus
-- ----------------------------
DROP TABLE IF EXISTS `ky_role_menus`;
CREATE TABLE `ky_role_menus`  (
    `role_id` bigint NOT NULL,
    `menus_id` bigint NOT NULL,
    PRIMARY KEY (`role_id`, `menus_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_user
-- ----------------------------
DROP TABLE IF EXISTS `ky_user`;
CREATE TABLE `ky_user`  (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_by` int NOT NULL,
    `create_time` datetime(3) NULL,
    `update_by` int NOT NULL,
    `update_time` datetime(3) NULL,
    `avatar` varchar(128) DEFAULT NULL,
    `email` varchar(128) DEFAULT NULL,
    `gender` tinyint(1) NOT NULL,
    `name` varchar(128) NOT NULL,
    `password` varchar(128) NOT NULL,
    `phone` varchar(32) NOT NULL,
    `status` tinyint(1) NOT NULL,
    `username` varchar(128) NOT NULL,
    `dept_id` bigint NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `ky_user_phone_uk`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ky_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `ky_user_roles`;
CREATE TABLE `ky_user_roles`  (
    `user_id` bigint NOT NULL,
    `roles_id` bigint NOT NULL,
    PRIMARY KEY (`user_id`, `roles_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;