# 项目基础设施

> Spring Boot 版本

## 介绍

用于构建学习和项目基础设施

已经实现的功能：

1. 用户/角色/权限关系
2. Security安全校验
3. JWT令牌授权
4. refreshToken刷新令牌
5. logout退出登录
6. AOP日志捕获

未来待实现的功能：

1. 加入前端页面
2. 加入菜单栏权限功能
3. ...

## 主要涉及技术

- Spring Boot 3.0.6
- Spring Security 6.0.3
- java-jwt 4.4.0
- Spring Data Jpa 3.0.5
- MySQL 8.0.32
- Spring Data Redis 3.0.5
- Knife4j 4.1.0

## 启动

打开本项目

1. 找到配置文件`application-dev.yml`，配置`MySQL`和`Redis`，数据库名字自己命名一个，比如：`base-project`
2. 找到启动类`AuthApplication`，直接启动就行了，数据库会自动生成数据库表文件
3. 找到数据库文件`base-project.sql`，初始化一些测试数据
4. 启动完成，浏览器[打开Knife4j文档](http://localhost:8080/doc.html)或者[打开Swagger文档](http://localhost:8080/swagger-ui/index.html)

## 账号

- 管理员：admin/123456
- 管理用户：manager/123456
- 普通用户：user/123456

> Tips: 源于开源，回馈于开源