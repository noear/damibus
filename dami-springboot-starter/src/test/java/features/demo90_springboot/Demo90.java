package features.demo90_springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
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
    public void main() {
        DamiBus<String, String> bus = Dami.<String, String>bus();

        System.out.println(bus.call("user.demo", "solon"));
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<Message<String, String>> {
        @Override
        public void onEvent(Message<String, String> payload) throws Throwable {
            if (payload.requiredReply()) {
                payload.reply("Hi " + payload.getContent());
            }
        }
    }
}
