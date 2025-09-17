package features.demo90_springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Event;
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
        DamiBus bus = Dami.bus();

        System.out.println(bus.send("user.demo", new RequestPayload<>("solon"))
                .getPayload()
                .getReceiver()
                .get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<Event<RequestPayload<String, String>>> {
        @Override
        public void onEvent(Event<RequestPayload<String, String>> event) throws Throwable {
            event.getPayload().getReceiver().complete("Hi " + event.getPayload().getContext());
        }
    }
}
