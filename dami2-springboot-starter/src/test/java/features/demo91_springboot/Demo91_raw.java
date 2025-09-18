package features.demo91_springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.spring.boot.annotation.DamiTopic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("features.demo91_springboot")
public class Demo91_raw {
    @Test
    public void main() throws Exception {
        DamiBus bus = Dami.bus();

        System.out.println(bus.send("user.demo", new CallPayload<>("solon"))
                .getPayload()
                .getSink()
                .get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements EventListener<CallPayload<String, String>> {
        @Override
        public void onEvent(Event<CallPayload<String, String>> event) throws Throwable {
            event.getPayload().getSink().complete("Hi " + event.getPayload().getData());
        }
    }
}
