package features.demo80_solon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonJUnit5Extension;

@ExtendWith(SolonJUnit5Extension.class)
public class Demo80 {
    @Test
    public void main() {
        DamiBus<String, String> bus = Dami.<String, String>bus();

        System.out.println(bus.sendAndRequest("user.demo", "solon"));

        bus.sendAndSubscribe("user.demo", "dami", rst -> {
            System.out.println(rst);
        });
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<Payload<String, String>> {
        @Override
        public void onEvent(Payload<String, String> payload) throws Throwable {
            if (payload.isSubscribe() || payload.isRequest()) {
                payload.reply("Hi " + payload.getContent());
                payload.reply("Hi " + payload.getContent());
            }
        }
    }
}
