# 项目简介:
网上预约挂号系统是为解决挂号难等就医难题开发出的项目，本项目包含后台管理系统和前台用户系统，采用前后端分离开发模式，主要使用的是SSM框架。

# 主要技术

## 项目后端
* 采用主流的SpringBoot+SpringCloud微服务架构，使用MyBatis-Plus做MySQL数据库的增删改查，使用Redis缓存数据，使用MongoDB实现高并发读写；
* 使用Gateway网关处理，使用Nacos作为注册中心，使用Feign做远程接口调用；
* 同时项目整合消息了中间件RabbitMQ，还添加了定时任务，实现就医提醒功能；
* 另外，项目综合应用了阿里云短信验证码服务、OSS以及微信登录、微信支付，同时增加了微信退款功能。
## 项目前端
采用主流前端框架Vue，使用Element-ui和Nuxt进行页面布局，Npm进行依赖管理，axios进行异步调用，使用ECharts进行图表显示。
# 主要接口示例:
> 医院设置后台管理模块、数据字典模块、用户管理模块、订单管理模块等。
