<h1 align="center" style="text-align:center;">
  DamiBus
</h1>
<p align="center">
	<strong>本地多模块过程调用框架（主打解耦）</strong>
</p>

<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/org.noear/dami">
        <img src="https://img.shields.io/maven-central/v/org.noear/dami.svg?label=Maven%20Central" alt="Maven" />
    </a>
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.txt">
		<img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" alt="jdk-8+" />
	</a>
    <br />
    <a target="_blank" href='https://gitee.com/noear/dami/stargazers'>
        <img src='https://gitee.com/noear/dami/badge/star.svg' alt='gitee star'/>
    </a>
    <a target="_blank" href='https://github.com/noear/dami/stargazers'>
        <img src="https://img.shields.io/github/stars/noear/dami.svg?logo=github" alt="github star"/>
    </a>
</p>

<br/>
<p align="center">
	<a href="https://jq.qq.com/?_wv=1027&k=kjB5JNiC">
	<img src="https://img.shields.io/badge/QQ交流群-22200020-orange"/></a>
</p>


<hr />




DamiBus，专为本地多模块之间通讯解耦而设计（尤其是未知模块、隔离模块、领域模块）。零依赖。

###  特点

结合总线与响应流的概念，可作事件分发，可作接口调用，可作异步响应。

* 支持事务传导（同步分发、异常透传）
* 支持事件标识、拦截器（方便跟踪）
* 支持监听者排序、附件传递（多监听时，可相互合作）
* 支持 Bus 和 Api 两种体验风格


### 与常见的 EventBus、ApiBean 的区别

|    | DamiBus | EventBus | Api | DamiBus 的情况说明                                                    |
|----|------|----------|-----|----------------------------------------------------------------|
| 广播 | 有    | 有        | 无   | 发送(send) + 监听(listen)<br/>以及 Api 模式                            |
| 应答 | 有    | 无        | 有   | 发送并请求(sendAndRequest) + 监听(listen) + 答复(reply)<br/>以及 Api 模式 |
| 回调 | 有+   | 无        | 有-  | 发送并订阅(sendAndSubscribe) + 监听(listen) + 答复(reply)               |
| 耦合 | 弱-   | 弱+       | 强++ |                                                                |


### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>1.0.0</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 简单示例


#### demo21_send

```java
//总线风格。bus()
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.bus().listen(topic, payload -> {
            System.err.println(payload); //可以有多个订阅
        });
        Dami.bus().listen(topic, payload -> {
            CompletableFuture.runAsync(()-> { //也可以异步消费
                System.err.println(payload);
            });
        });


        //发送事件
        Dami.bus().send(topic, "{name:'noear',say:'hello'}");
    }
}
```

#### demo12_request

```java
//泛型总线风格。<C,R>bus()
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.<String,String>bus().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest() || payload.isSubscribe()) {
                payload.reply("hi!"); // sendAndRequest 只接收第一个
                payload.reply("* hi nihao!");
                payload.reply("** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.<String,String>bus().sendAndRequest(topic, "world"); //要求有返回值
        System.out.println(rst1);

        Dami.<String,String>bus().sendAndSubscribe(topic, "world", rst2 -> {
            System.out.println(rst2); //subscribe 不限回调次数
        });
    }
}
```

#### demo31_api

使用 ioc 适配版本更简便，详情：[dami-solon-plugin](dami-solon-plugin)、[dami-springboot-starter](dami-springboot-starter)

```java
//接口风格
public interface EventUser {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口，但会带来依赖关系）
public class EventUserListener1 { // implements EventUser
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
    }

    public Long getUserId(String name) {
        return Long.valueOf(name.hashCode());
    }
}

public class Demo31 {
    public static void main(String[] args) {
        //注册监听器
        EventUserListener1 userEventListener = new EventUserListener1();
        api.registerListener(topicMapping, userEventListener);

        //生成发送器
        EventUser eventUser = api.createSender(topicMapping, EventUser.class);

        //发送测试
        eventUser.onCreated(1L, "noear");
        Long userId = eventUser.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        api.unregisterListener(topicMapping, userEventListener);
    }
}
```


### 定制能力（详见主题路由器：[TopicRouter.md](TopicRouter.md)）

```java
public class Demo15_path {
    public void main(){
        //切换为模式匹配路由器 + RoutingPath（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
        DamiConfig.configure(new TopicRouterPatterned(RoutingPath::new));

        //拦截
        Dami.bus().listen("demo/a/*", (payload) -> {
            System.err.println(payload);
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
        Dami.bus().listen("demo.a:id", (payload) -> {
            System.err.println(payload);
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