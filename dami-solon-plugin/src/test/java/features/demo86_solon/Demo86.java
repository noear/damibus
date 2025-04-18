package features.demo86_solon;


import org.junit.jupiter.api.Test;

import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo86 {
    @Test
    public void main() {
        Dami.bus().send("demo86.event.user", new User(85));
        Dami.bus().send("demo86.event.user-no", new User(85));
    }

    @DamiTopic("demo86.event.user")
    public static class UserListener implements TopicListener<Payload<User, Object>> {

        @Override
        public void onEvent(Payload<User, Object> payload) throws Throwable {
            System.out.println(payload.getContent());
        }
    }
}
