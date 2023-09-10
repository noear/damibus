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
* 支持附件传导（多个监听者之间，可通过附件传递信息）
* 支持事件跟踪标识
* 支持监听者排序
* 支持纯弱类型总线通讯（支持类隔离的场景）
* 支持泛型、强类型总线
* 支持 RPC 风格的接口体验（支持自定义编解码）
* 支持拦截器


### 与常见的 EventBus 的区别

* 相同（都有）

发送（send）与监听（listen）接口。//大家名字取得可能略有不同

* 不同（多了）

请求并等响应（requestAndResponse）、请求并等回调（requestAndCallback）、响应（response）接口

* 还有

提供 Bus 接口之外，还提供了 Api 风格的操作界面（像 dubbo、feign 一样使用）

### 很酷的代码演示（创意满满）

提供了三个操作界面（也可以自己包装界面），下面会分别演示

* Dami.busStr() 提供弱类型总线操作的界面（适合类隔离的场景）
* Dami.bus() 提供泛型、强类型总线操作的界面
* 
* Dami.api() 提供 RPC 风格的操作界面（像 dubbo、feign 一样使用事件总线；支持自定义编解码）


### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.19.3</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 示例

更多的示例请参考：[example-dami](example-dami) 模块。

#### demo11_event

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
                Dami.busStr().response(payload, "hi nihao!");
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
        UserEventListenerImpl userEventListener = new UserEventListenerImpl();
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
```