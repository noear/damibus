package features.demo12_request;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.api.DamiApi;

import java.util.concurrent.CompletableFuture;

public class Demo12_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiApi api = Dami.newApi();

    @Test
    public void main() throws Exception {

        //监听事件
        api.<String, String>handle(topic, (req, resp) -> {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread());
                System.err.println(req);

                resp.complete("hi!");
            });
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = api.<String, String>call(topic, "world").get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
