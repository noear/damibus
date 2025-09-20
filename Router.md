关于事件路由的说明

## 1、可以定制的事件路由器

目前事件路由器，分为：

* 默认路由器（基于 hash 实现，速度快）
* 模式匹配路由器

如果有需求，程序启动时配置一下：

```java
DamiConfig.configure(new PathTopicEventRouter());
```


## 2、事件路由器的监听风格

### 2.1、默认路由器

* Hash （监听是什么，收的就是什么）

| 发送主题                    | 监听主题                  | 备注               |
|-------------------------|-----------------------|------------------|
| `event.user.created`    | `event.user.created`  | 支持 ApiBean 的模式调用 |



### 2.2、模式匹配路由器

支持路由定制，已实现的有：RoutingPath，路径风格的路由；RoutingTag，标签风格的路由。还可以自己定制

* RoutingPath（`*` 代表一段，`**` 代表不限断）

| 发送主题                  | 监听主题              | 备注  |
|-----------------------|-------------------|-----|
| `event/user/created`  | `event/*/created` |     |
| `event/order/created` | `event/*/created` |     |


* RoutingTag（`:` 为主题与标签的间隔符，`,` 为主题的间隔符）


| 发送主题                             | 监听主题                   | 监听Tag | 是否监听到  |
|----------------------------------|----------------------------|----------|----------|
| `event.user.update`       | `event.user.update`        | 不限 | 是 |
| `event.user.update:id`       | `event.user.update`        | 不限 | 是 |
| `event.user.update`       | `event.user.update:id`        | 不限 | 是 |
| `event.user.update:id` | `event.user.update:id`        |     id     | 是 |
| `event.user.update:id,name ` | `event.user.update:name ` |     name     | 是 |
| `event.user.update:id` | `event.user.update:name`        |     name     | 否 |
