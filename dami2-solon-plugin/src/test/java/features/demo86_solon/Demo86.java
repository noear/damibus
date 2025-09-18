package features.demo86_solon;


import org.junit.jupiter.api.Test;

import org.noear.dami2.Dami;
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo86 {
    @Test
    public void main() {
        Dami.bus().send("demo86.event.user", new User(85));
        Dami.bus().send("demo86.event.user-no", new User(85));
    }

    @DamiTopic("demo86.event.user")
    public static class UserListener implements EventListener<User> {

        @Override
        public void onEvent(Event<User> event) throws Throwable {
            System.out.println(event.getPayload());
        }
    }
}
