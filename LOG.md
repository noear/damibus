#### 0.31

* 添加 TopicDispatcher，将调度处理与路由器开发；更方便添加监控
* 添加 InterceptorChain::getTargets 接口，方便打印日志
* 添加 Acceptor::isSingle 接口，用于识别单发还是多发接收
* 调整 sendAndResponse 更名为 sendAndRequest
* 调整 sendAndCallback 更名为 sendAndSubscribe

#### 0.30

* 调整 启用新的配置方式（配置路由时，不需要重新实例化总线）
* 调整 接口默认方法处理策略（有订阅执行订阅，无订阅者执行默认）
* 调整 createSender 调整类加载器的主体
* 调整 send,sendAndCallback 返回类型改为 bool，表示是否有订阅处理

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

