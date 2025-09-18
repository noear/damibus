<h1 align="center" style="text-align:center;">
  DamiBus
</h1>
<p align="center">
	<strong>单本多模块过程调用框架（主打解耦）</strong>
</p>

<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/org.noear/dami2">
        <img src="https://img.shields.io/maven-central/v/org.noear/dami2.svg?label=Maven%20Central" alt="Maven" />
    </a>
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.txt">
		<img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8-green.svg" alt="jdk-8" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-11-green.svg" alt="jdk-11" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-17-green.svg" alt="jdk-17" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-21-green.svg" alt="jdk-21" />
	</a>
    <br />
    <a target="_blank" href='https://gitee.com/noear/dami/stargazers'>
        <img src='https://gitee.com/noear/dami/badge/star.svg' alt='gitee star'/>
    </a>
    <a target="_blank" href='https://github.com/noear/dami/stargazers'>
        <img src="https://img.shields.io/github/stars/noear/dami.svg?style=flat&logo=github" alt="github star"/>
    </a>
</p>

<br/>
<p align="center">
	<a href="https://jq.qq.com/?_wv=1027&k=kjB5JNiC">
	<img src="https://img.shields.io/badge/QQ交流群-22200020-orange"/></a>
</p>


<hr />




DamiBus，专为单体多模块之间通讯解耦而设计（尤其是未知模块、隔离模块、领域模块）。零依赖。

###  特点

结合总线与响应的概念，可作事件分发，可作接口调用，可作响应式流生成，等等。

* 支持事务传导（同步分发、异常透传）
* 支持拦截器（方便跟踪）
* 支持监听者排序
* 支持附件传递（多监听时，可相互合作）
* 支持回调和响应式流
* 支持 Bus 和 Lpc 两种体验风格


### 与常见的 EventBus、ApiBean 的区别

|       | DamiBus | EventBus | ApiBean | 
|-------|---------|----------|---------|
| 广播    | 有       | 有        | 无       | 
| 请求与响应 | 有       | 无        | 有       | 
| 响应式流  | 有       | 无        | 有       | 
| 耦合    | 弱-      | 弱+       | 强++     |     


### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami2</artifactId>
    <version>2.0.0-M1</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 简单示例


#### demo21_send

```java
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.bus().listen(topic, event -> {
            System.err.println(event.getPayload()); //可以有多个订阅
        });

        //发送事件
        Dami.bus().send(topic, "{name:'noear',say:'hello'}");
    }
}
```

#### demo12_call

```java
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) throws Exception {
        //监听事件（调用事件）
        Dami.bus().<String, String>listen(topic, (event, data, sink) -> {
            System.err.println(data);

            sink.complete("hi!");
        });


        //发送事件（调用）
        String rst1 = Dami.bus().<String, String>call(topic, "world").get();
        //发送事件（调用） //支持应急处理（当没有订阅时启用）
        //String rst1 = Dami.bus().<String, String>call(topic, "world", r -> r.complete("def")).get();
        System.out.println(rst1);
    }
}
```

#### demo31_lpc

使用 ioc 适配版本更简便，详情：[dami-solon-plugin](dami-solon-plugin)、[dami-springboot-starter](dami-springboot-starter)

```java
//服务消费者接口
public interface UserService {
    void onCreated(Long userId, String name);

    Long getUserId(String name);
}

//通过约定保持与 UserService 相同的接口定义（或者实现 UserService 接口，但会带来依赖关系）
public class UserServiceImpl { // implements UserService
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
    }

    public Long getUserId(String name) {
        return Long.valueOf(name.hashCode());
    }
}

public class Demo31 {
    public static void main(String[] args) {
        //注册服务实现
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        Dami.lpc().registerService(topicMapping, userServiceImpl);

        //创建服务消费者（接口代理）
        UserService userService = Dami.lpc().createConsumer(topicMapping, UserService.class);

        //发送测试
        userService.onCreated(1L, "noear");
        Long userId = userService.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销服务实现
        Dami.lpc().unregisterService(topicMapping, userServiceImpl);
    }
}
```


### 定制能力（详见事件路由器：[Router.md](Router.md)）

```java
public class Demo15_path {
    public void main(){
        //切换为模式匹配路由器 + RoutingPath（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
        DamiConfig.configure(new TopicRouterPatterned(RoutingPath::new));

        //拦截
        Dami.bus().listen("demo/a/*", (event) -> {
            System.err.println(event.getPayload());
        });

        //发送事件
        Dami.bus().send("demo/a/1", "world1");
        Dami.bus().send("demo/a/2", "world2");
    }
}
```

```java
public class Demo15_tag {
    public void main(){
        //切换为模式匹配路由器 + RoutingTag（":"前为主题，后按 "," 号分割作为tag）
        DamiConfig.configure(new TopicRouterPatterned(RoutingTag::new));

        //拦截
        Dami.bus().listen("demo.a:id", (event) -> {
            System.err.println(event.getPayload());
        });

        //发送事件
        Dami.bus().send("demo.a:id", "world1");
        Dami.bus().send("demo.a:id,name", "world2");
    }
}
```

### 可无依赖接口实现

详情：[dami-solon-plugin](dami-solon-plugin)、[dami-springboot-starter](dami-springboot-starter)

```java
@DamiTopic("event.user")
public interface EventUserService {
    User getUser(long userId); //方法的主题 = topicMapping + "." + method.getName() //方法不能重名
}

//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("event.user")
public class EventUserServiceListener { // implements EventUserService // 它相当于是个实现类
    public User getUser(long userId) {
        return new User(userId);
    }
}

@SolonTest
public class Demo81 {
    @Inject
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        assert user.getUserId() == 99;
    }
}
```