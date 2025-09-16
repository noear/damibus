package features.demo23_unlistenall;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo23 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<String,String>bus()
    DamiBus<String> bus = new DamiBusImpl<>();

    @Test
    public void main() throws InterruptedException {
        final TopicListener<Message<String>> aListener = message -> {
            System.out.println("i'm a:" + message);
        };
        final TopicListener<Message<String>> bListener = message -> {
            System.out.println("i'm b:" + message);
        };

        final TopicListener<Message<String>> cListener = message -> {
            System.out.println("i'm b:" + message);
        };
        //监听事件
        bus.listen(topic, aListener);
        bus.listen(topic, bListener);
        bus.listen(topic, cListener);
        System.out.println("------------------ 从未移除监听器之前 ------------------");
        //发送事件
        bus.send(topic, "aaaaaa");
        System.out.println("------------------ 移除 a 监听器之后 ------------------");
        bus.unlisten(topic, aListener);
        bus.send(topic, "bbbbbb");
        System.out.println("------------------ 移除所有监听器之后 ------------------");
        bus.unlisten(topic);
        bus.send(topic, "cccccc");


        // 验证多线程情况下
        CountDownLatch listen = new CountDownLatch(3);
        CompletableFuture.runAsync(()->{
            bus.listen(topic, aListener);
            listen.countDown();
        });
        CompletableFuture.runAsync(()->{
            bus.listen(topic, bListener);
            listen.countDown();
        });
        CompletableFuture.runAsync(()->{
            bus.listen(topic, cListener);
            listen.countDown();
        });
        assert listen.await(1, TimeUnit.SECONDS);

        System.out.println("------------------ runAsync 从未移除监听器之前 ------------------");
        //发送事件
        bus.send(topic, "runAsync aaaaaa");
        System.out.println("------------------ runAsync 移除 a 监听器之后 ------------------");
        CountDownLatch unlisten = new CountDownLatch(2);
        CompletableFuture.runAsync(()->{
            bus.unlisten(topic, aListener);
            unlisten.countDown();
        });
        CompletableFuture.runAsync(()->{
            // 等待
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            bus.send(topic, "runAsync bbbbbb");
            unlisten.countDown();
        });
        assert unlisten.await(2, TimeUnit.SECONDS);

        CountDownLatch unlistenall = new CountDownLatch(2);

        System.out.println("------------------ runAsync 移除所有监听器之后 ------------------");
        CompletableFuture.runAsync(()->{
            bus.unlisten(topic);
            unlistenall.countDown();
        });
        CompletableFuture.runAsync(()->{
            // 等待
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            bus.send(topic, "runAsync cccccc");
            unlistenall.countDown();
        });
        assert unlistenall.await(2, TimeUnit.SECONDS);
    }
}
