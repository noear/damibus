#### 1.0.6

* 调整 no matching listener 的日志级别（warn 改为 debug）

#### 1.0.5

* 添加 `DamiBus:sendAndRequest(topic, content, def)` 方法

#### 1.0.4

* 添加 `Dami.newBus()` 新建实例方法

#### 1.0.3

* DamiBus:sendAndRequest 添加默认返回支持

#### 1.0.2

* dami-solon-plugin DamiTopic 注解的类，增加代理支持

#### 1.0.1

* 添加 `@Param` 注解支持（支持没有 `-parameters` 的编译场景）

#### 1.0.0

* 调整 dami DamiApi::registerListener 改为公有函数注册（之前为自有函数注册）
* 调整 dami-solon-plugin solon 升为 3.0.1
* 添加 dami-springboot-starter 新注解 `@DamiScan`

#### 0.58
* 调整 synchronized 锁改为 ReentrantLock 锁

#### 0.57
* 调整 slf4j 依赖改为 provided
* solon 升为 2.7.0


#### 0.56
* solon 升为 2.6.2

#### 0.55
* 添加 DamiBus::sendAndRequest 带超时的接口

#### 0.54
* 添加 IdGenerator 接口（支持自定义id生成）
* 调整 Payload::getGuid 更名为 getPlid
* 调整 MDC（dami-guid）更名为 plid


#### 0.53
* 添加 DamiBus::unlisten 取肖主题的所有监听

#### 0.52
* 修复 TopicDispatcherDefault::dispatch MDC 添加后没有移除的问题

#### 0.51
* 添加 TopicDispatcher，将调度处理与路由器开发（更方便添加监控）
* 添加 InterceptorChain::getTargets 接口（可以知道有哪些订阅）
* 添加 Acceptor::isSingle 接口，用于识别单发还是多发接收
* 添加 Payload::isSubscribe 接口，用于识别订阅
* 
* 更名 sendAndResponse 为 sendAndRequest!!!
* 更名 sendAndCallback 为 sendAndSubscribe!!!
* 删除 Dami::intercept 接品，改用 Dami.bus()::intercept

#### 0.30

* 调整 启用新的配置方式（配置路由时，不需要重新实例化总线）
* 调整 接口默认方法处理策略（有订阅执行订阅，无订阅者执行默认）
* 调整 createSender 调整类加载器的主体
* 调整 send,sendAndSubscribe 返回类型改为 bool（马上可以知道是否有订阅目标）

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

