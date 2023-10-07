#### 0.30

* 添加接口默认方法是否发送的开关

#### 0.29

* 添加 DamiTopic::index() 属性
* 修复 dami-springboot-starter 实现类被代理后不能正常注册与注销的问题

#### 0.28

* TopicRouterPatterned 增加排序支持
* TopicRouterPatterned 分离路由能力，可定制
* 增加基于 Tag 的路由模式定制
* dami-springboot-starter，增加 spronboot 2.0 的兼容

#### 0.27

* 增加模式匹配路由器
* 提供可切换实现的机制（方便用户定制）

#### 0.26

* 增加日志打印
* 增加MDC（dami-guid）

#### 0.24

* Payload::reply 增加返回是否成功
* Dami.api() 的监听者参数数量可与发送者略有不同（比如增加 Payload 参数）

#### 0.23

* 调整 springboot 适配，让 @DamiTopic 可以单独使用

#### 0.22

* 增加附件支持
* 增加泛型支持
* 增加监听锁
* 增加 API 接口风格
* 增加 DamiBus 增加带顺序位的监听接口
* 主题路由器 add, remove 接口增加同步锁

