# SecKill
### 秒杀系统，高并发技术解决方案<br>
该项目实现的是一个商品秒杀系统，通过一系列的优化，提高其应对高并发的能力<br>
### 开发环境与技术
1. SpringBoot2.0.1 + MyBatis + MySQL + Durid
2. Redis + RabbitMQ
3. 阿里大于
4. BootStrap + Thymeleaf
5. JSR303参数校验 + 全局异常处理器
6. 分布式Session
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
3. 接口的防刷限流<br>
### 具体技术的实现
1. [SpringBoot学习之高并发接口优化—–秒杀接口地址隐藏(验证码)+接口限流防刷](https://songwell1024.github.io/2018/08/09/SecurityOptimise/)<br>

2. [使用RabbitMQ改写秒杀功能](https://songwell1024.github.io/2018/08/08/APIIOptimise/)<br>

3. [SpringBoot2之秒杀页面优化及解决超卖问题](https://songwell1024.github.io/2018/08/05/SecKillPageOptimise/)<br>

4. [SpringBoot使用JSR303参数校验并进行全局异常处理](https://songwell1024.github.io/2018/08/02/JSR303/)<br>

5. [SpringBoot2.0集成Redis详解及踩过的坑（Could not get a resource from the pool）](https://songwell1024.github.io/2018/08/02/Redis/)<br>

6. [SpringBoot2踩坑之SpringWebContext方法过时](https://songwell1024.github.io/2018/08/05/SpringWebContext/)<br>

7. [基于阿里大于的短信验证微服务](https://songwell1024.github.io/2019/01/06/CheckSmsCode/)

  [具体的技术详情可以访问我的博客](https://songwell1024.github.io)
