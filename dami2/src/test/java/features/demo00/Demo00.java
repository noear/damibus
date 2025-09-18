package features.demo00;

import org.noear.dami2.Dami;
import reactor.core.publisher.Flux;

/**
 * @author noear 2023/10/7 created
 */
public class Demo00 {
    static String topic = "demo.hello";

    public void case_send() {
        //监听事件
        Dami.bus().listen(topic, event -> {
            System.err.println(event.getPayload());
        });

        //发送事件
        Dami.bus().send(topic, "hello");
    }

    public void case_call() throws Exception {
        //监听调用事件
        Dami.bus().listen(topic, (event, data, future) -> {
            System.err.println(data);
            future.complete("hi!");
        });

        //发送调用事件
        String rst = Dami.bus().<String, String>call(topic, "hello").get();
        System.err.println(rst);
    }

    public void case_stream() {
        //监听流事件
        Dami.bus().<String, String>listen(topic, (event, att, data, subscriber) -> {
            System.err.println(data);
            subscriber.onNext("hi");
            subscriber.onComplete();
        });

        //发送流事件
        Flux.from(Dami.bus().<String, String>stream(topic, "hello")).doOnNext(item -> {
            System.err.println(item);
        });
    }

    public interface UserService {
        Long getUserId(String name);
    }

    public class UserServiceImpl {
        public Long getUserId(String name) {
            return 99L;
        }
    }

    public void case_lpc() {
        //注册服务实现（监听问答事件）
        Dami.lpc().registerService("demo", new UserServiceImpl());

        //生成服务消费者（发送问题事件）
        UserService userService = Dami.lpc().createConsumer("demo", UserService.class);
        userService.getUserId("noear");
    }
}
