<h1 align="center" style="text-align:center;">
  Dami
</h1>
<p align="center">
	<strong>基于事件总线的本地过程调用框架</strong>
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




Dami，专为本地多模块之间通讯解耦而设计（尤其是未知模块、隔离模块、领域模块）。零依赖，特适合 DDD。

###  特点

结合 Bus 与 RPC 的概念，可作事件分发，可作接口调用，可作异步响应。

* 支持事务传导（同步分发、异常透传）
* 支持事件标识、拦截器（方便跟踪）
* 支持监听者排序、附件传递（多监听时，可相互合作）
* 支持 Bus 和 Api 两种体验风格


### 与常见的 EventBus、ApiBean 的区别

|    | Dami | EventBus | ApiBean | Dami 的情况说明                                      |
|----|------|----------|---------|-------------------------------------------------|
| 广播 | 有    | 有        | 无       | 发送（send）+ 监听（listen）<br/>以及 Api 模式              |
| 应答 | 有    | 无        | 有       | 请求并等响应（requestAndResponse）+ 响应（response）<br/>以及 Api 模式 |
| 回调 | 有+   | 无        | 有-      | 请求并等回调（requestAndCallback）+ 响应（response）        |
| 耦合 | 弱-   | 弱+       | 强++     |                                                 |


### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.20.1</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 示例


#### demo11_send

```java
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.busStr().send(topic, "world");
    }
}
```

#### demo12_request

```java
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest()) {
                Dami.busStr().response(payload, "hi nihao!"); // requestAndResponse 只接收第一个
                Dami.busStr().response(payload, "* hi nihao!");
                Dami.busStr().response(payload, "** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.busStr().requestAndResponse(topic, "world");
        System.out.println(rst1);

        Dami.busStr().requestAndCallback(topic, "world", rst2 -> {
            System.out.println(rst2); //callback 可不限返回
        });
    }
}
```

#### demo31_api

```java
public interface UserEventSender {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}

public class UserEventListenerImpl {
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
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        Dami.api().registerListener("demo.user", userEventListener);

        //生成发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
```