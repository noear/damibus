<h1 align="center" style="text-align:center;">
  Dami
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




Dami，专为本地多模块之间通讯解耦而设计（尤其是未知模块、类隔离模块）。适用于任何 java 开发环境。（有可能会推出多语言版本）

### 与常见的 EventBus 的区别

* 相同（都有）

发送（send）与监听（listen）接口

* 不同（多了）

请求并等响应（requestAndResponse）、请求并等回调（requestAndCallback）、响应（response）接口

### 代码演示

* 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.4</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

* 代码

```java
public class DemoApp {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) throws Throwable {
        TopicListener<Payload> listener = createListener();

        //监听
        DamiBus.global().listen(demo_topic,listener);

        //发送测试
        sendTest();

        //取消监听
        DamiBus.global().unlisten(demo_topic,listener);
    }

    //创建监听器
    private static TopicListener<Payload> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                DamiBus.global().response(payload, "你发了：" + payload.getContent());
            }
        };
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        DamiBus.global().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = DamiBus.global().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        DamiBus.global().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
```
