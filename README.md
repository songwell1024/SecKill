# SecKill
### 秒杀系统，高并发技术解决方案<br>
该项目实现的是一个商品秒杀系统，通过一系列的优化，提高其应对高并发的能力<br>
### 开发环境与技术
1. SpringBoot2.0.1 + MyBatis + MySQL + Durid
2. Redis + RabbitMQ
3. BootStrap + Thymeleaf
4. JSR303参数校验 + 全局异常处理器
5. 分布式Session
### 秒杀优化
**页面的优化：**
1. 页面缓存 +  URL缓存 + 对象缓存，减少数据库的访问
2. 页面静态化，前后端分离
3. 静态资源优化
**秒杀接口优化**
1. RabbitMQ队列缓冲异步下单
2. Redis预减库存
3. 内存标记
**安全优化**
1. 秒杀地址隐藏
2. 数学公式验证码缓冲
3. 接口的防刷限流
具体的技术详情可以访问我的博客[](https://songwell1024.github.io)
