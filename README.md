<h1 align="center" style="text-align:center;">
  DamiBus
</h1>
<p align="center">
	<strong>本地过程调用框架</strong>
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




DamiBus，专为本地多模块之间通讯解耦而设计（尤其是未知模块、类隔离模块）。零依赖，12k 大小。

###  特点

结合 Bus 与 RPC 的概念，可作事件分发，可作接口调用，可作通知回调。

* 支持事务传导（同步分发、异常透传）
* 支持附件传导（多个监听者之间，可通过附件传递信息）
* 支持事件跟踪标识
* 支持监听者排序
* 支持纯弱类型通讯（支持类隔离的场景）
* 支持泛型、强类型



### 与常见的 EventBus 的区别

* 相同（都有）

发送（send）与监听（listen）接口

* 不同（多了）

请求并等响应（requestAndResponse）、请求并等回调（requestAndCallback）、响应（response）接口


### 代码演示

提供了两个全局实例（也可以自己实例化总线），会分别演示

* DamiBus.str() 提供弱类型支持（适合类隔离的场景）
* DamiBus.obj() 提供泛型、强类型支持


#### 1、添加依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.6</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

#### 2、弱类型代码示例（适合类隔离的场景）

```java
public class StringDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        TopicListener<Payload<String, String>> listener = createListener();

        //监听
        DamiBus.str().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        DamiBus.str().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<String, String>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                DamiBus.str().response(payload, "你发了：" + payload.getContent());
            }
        };
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        DamiBus.str().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = DamiBus.str().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        DamiBus.str().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
```

#### 3、泛型、强类型代码示例


```java
public class ObjDemo {
    static String demo_topic = "demo.user.info";

    public static void main(String[] args) {
        TopicListener<Payload<User, User>> listener = createListener();

        //监听
        DamiBus.<User, User>obj().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        DamiBus.<User, User>obj().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<User, User>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                final User content = payload.getContent().sing("你太美");
                //如果是请求载体，再响应一下
                DamiBus.<User, User>obj().response(payload, content);
            }
        };
    }

    //发送测试
    private static void sendTest() {
        final User user = new User().name("kk").age(2.5).hobby(new String[]{"唱", "跳", "rap", "打篮球"});
        //普通发送
        DamiBus.<User, Void>obj().send(demo_topic, user);

        //普通发送,自定义构建参数
        DamiBus.<User, Void>obj().send(new Payload<>("123", demo_topic, user));

        //请求并等响应
        User rst1 = DamiBus.<User, User>obj().requestAndResponse(demo_topic, user);
        System.out.println("响应返回: " + rst1);

        user.sing("ai kun");
        //请求并等回调
        DamiBus.<User, User>obj().requestAndCallback(demo_topic, user, rst2 -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
```