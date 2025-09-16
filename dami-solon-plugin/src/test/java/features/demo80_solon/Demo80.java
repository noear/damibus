package features.demo80_solon;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo80 {
    @Test
    public void main() {
        DamiBus<String, String> bus = Dami.<String, String>bus();

        System.out.println(bus.call("user.demo", "solon"));
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<Message<String, String>> {
        @Override
        public void onEvent(Message<String, String> message) throws Throwable {
            if (payload.requiredReply()) {
                payload.reply("Hi " + payload.getContent());
            }
        }
    }
}
