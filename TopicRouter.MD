关于主题路由的说明

## 1、可以定制的主题路由器

目前主题路由器，分为：

* 默认路由器（基于 hash 实现，速度快）
* 模式匹配路由器

如果有需求，程序启动时配置一下：

```java
DamiConfig.configure(new TopicRouterPatterned(RoutingPath::new));
```


## 2、主题路由器的监听风格

### 默认路由器

* Hash （监听是什么，收的就是什么）

| 发送主题                    | 监听主题                  | 备注               |
|-------------------------|-----------------------|------------------|
| `event.user.created`    | `event.user.created`  | 支持 ApiBean 的模式调用 |



### 模式匹配路由器

支持路由定制，已实现的有：RoutingPath，路径分格的路由；RoutingTag，标签分格的路由。还可以自己定制

* RoutingPath（`*` 代表一段，`**` 代表不限断）

| 发送主题                  | 监听主题              | 备注  |
|-----------------------|-------------------|-----|
| `event/user/created`  | `event/*/created` |     |
| `event/order/created` | `event/*/created` |     |


* RoutingTag（`:` 为主题与标签的间隔符，`,` 为主题的间隔符）


| 发送主题                             | 监听主题                       | 备注       |
|----------------------------------|----------------------------|----------|
| `event.user.update:userId`       | `event.user.update`        | 没有标签表示不限 |
| `event.user.update:userId,label` | `event.user.update`        |          |
| `event.user.update:userId,label` | `event.user.update:userId` |          |