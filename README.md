# V免签 —— 个人开发者收款解决方案

本项目为[szvone/Vmq](https://github.com/szvone/Vmq)的重制版，基于JDK17 + SpringBoot3.2
规范设计了新的API，同时保留了原版API，因此兼容原版APK

新增功能：

- [x] docker一键部署
- [x] 多用户支持

计划功能：

- [ ] 配置多个收款码轮询收款，降低风控风险
- [ ] 用户管理

## 介绍

V免签 是基于 SpringBoot 实现的一套免签支付程序，主要包含以下特色功能：

- [x] 收款即时到账，无需进入第三方账户，收款更安全
- [x] 简单配置，一键搭建
- [x] 超简单Api使用，提供统一Api实现收款回调
- [x] 免费、开源，无后门风险
- [x] 支持监听店员收款信息，可使用支付宝微信小号/模拟器挂机，方便IOS用户
- [x] 免root，免xp框架，不修改支付宝/微信客户端，防封更安全

## 部署

### Docker部署 (推荐)

需要预先安装好`docker`和`docker-compose`

```bash
# 暂未开发完成，不提供下载包，有需要的可以自己编译构建
wget https://github.com/kales0202/vpay/releases/latest/download/vpay.tar.gz -O vpay.tar.gz
tar -zxvf vpay.tar.gz
docker-compose up -d
```

### 代码部署

需要预先安装好`JDK17`并设置环境变量

```bash
# 暂未开发完成，不提供下载包，有需要的可以自己编译构建
wget https://github.com/kales0202/vpay/releases/latest/download/vpay.tar.gz -O vpay.tar.gz
tar -zxvf vpay.tar.gz
nohup java -jar app/vpay.jar -Dspring.profiles.active=prod > vpay.log 2>&1 &
```

## 开发接入指南

TODO...

## 二次开发说明

- 使用`bootJar`命令编译构建，构建产物路径：`build/distributions/vpay.tar.gz`
- 已接入文档生成工具[smart-doc](https://github.com/TongchengOpenSource/smart-doc)