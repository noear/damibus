package features.demo90_springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.payload.RequestPayload;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("features.demo90_springboot")
public class Demo90 {
    @Test
    public void main() throws Exception {
        DamiBus<RequestPayload<String, String>> bus = Dami.bus();

        System.out.println(bus.send("user.demo", new RequestPayload<>("solon"))
                .getPayload()
                .getReceiver()
                .get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<Message<RequestPayload<String, String>>> {
        @Override
        public void onEvent(Message<RequestPayload<String, String>> message) throws Throwable {
            message.getPayload().getReceiver().complete("Hi " + message.getPayload().getContext());
        }
    }
}
