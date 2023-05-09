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

INSERT INTO `ky_permission` VALUES (1, 1, NOW(), 1, NOW(), '最高数据权限', 'admin:create');
INSERT INTO `ky_permission` VALUES (2, 1, NOW(), 1, NOW(), '最高数据权限', 'admin:read');
INSERT INTO `ky_permission` VALUES (3, 1, NOW(), 1, NOW(), '最高数据权限', 'admin:update');
INSERT INTO `ky_permission` VALUES (4, 1, NOW(), 1, NOW(), '最高数据权限', 'admin:delete');
INSERT INTO `ky_permission` VALUES (5, 1, NOW(), 1, NOW(), '普通数据权限', 'manager:create');
INSERT INTO `ky_permission` VALUES (6, 1, NOW(), 1, NOW(), '普通数据权限', 'manager:read');
INSERT INTO `ky_permission` VALUES (7, 1, NOW(), 1, NOW(), '普通数据权限', 'manager:update');
INSERT INTO `ky_permission` VALUES (8, 1, NOW(), 1, NOW(), '普通数据权限', 'manager:delete');

INSERT INTO `ky_role` VALUES (1, 1, NOW(), 1, NOW(), '管理员', 'ADMIN');
INSERT INTO `ky_role` VALUES (2, 1, NOW(), 1, NOW(), '管理用户', 'MANAGER');
INSERT INTO `ky_role` VALUES (3, 1, NOW(), 1, NOW(), '普通用户', 'USER');

INSERT INTO `ky_role_permissions` VALUES (1,1);
INSERT INTO `ky_role_permissions` VALUES (1,2);
INSERT INTO `ky_role_permissions` VALUES (1,3);
INSERT INTO `ky_role_permissions` VALUES (1,4);
INSERT INTO `ky_role_permissions` VALUES (1,5);
INSERT INTO `ky_role_permissions` VALUES (1,6);
INSERT INTO `ky_role_permissions` VALUES (1,7);
INSERT INTO `ky_role_permissions` VALUES (1,8);
INSERT INTO `ky_role_permissions` VALUES (2,5);
INSERT INTO `ky_role_permissions` VALUES (2,6);
INSERT INTO `ky_role_permissions` VALUES (2,7);
INSERT INTO `ky_role_permissions` VALUES (2,8);

INSERT INTO `ky_user` VALUES (1, 1, NOW(), 1, NOW(), 'admin@foxmail.com', '管理员', '$2a$10$dM9LzVx5iqB/JxrVEeAAi.BN/1n638UUiy3Gp9XLZwCkwG73KcyIy', '15012341000', 'ACTIVE', 'admin', 1);
INSERT INTO `ky_user` VALUES (2, 1, NOW(), 1, NOW(), 'manager@foxmail.com', '管理用户', '$2a$10$dM9LzVx5iqB/JxrVEeAAi.BN/1n638UUiy3Gp9XLZwCkwG73KcyIy', '15012341001', 'ACTIVE', 'manager', 2);
INSERT INTO `ky_user` VALUES (3, 1, NOW(), 1, NOW(), 'user@foxmail.com', '普通用户', '$2a$10$dM9LzVx5iqB/JxrVEeAAi.BN/1n638UUiy3Gp9XLZwCkwG73KcyIy', '15012341002', 'ACTIVE', 'user', 3);

