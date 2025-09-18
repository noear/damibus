package features.demo91_springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.bus.receivable.CallEventListener;
import org.noear.dami2.spring.boot.annotation.DamiTopic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

@ContextConfiguration
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("features.demo91_springboot")
public class Demo91 {
    @Test
    public void main() throws Exception {
        System.out.println(Dami.bus().call("user.demo", "solon")
                .get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements CallEventListener<String, String> {
        @Override
        public void onCall(Event<CallPayload<String, String>> event, String data, CompletableFuture<String> sink) {
            sink.complete("Hi " + data);
        }
    }
}
