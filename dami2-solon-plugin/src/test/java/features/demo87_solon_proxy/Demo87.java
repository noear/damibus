package features.demo87_solon_proxy;

import features.demo86_solon.User;
import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.solon.annotation.DamiTopic;
import org.noear.solon.data.annotation.Transaction;
import org.noear.solon.data.tran.TranPolicy;
import org.noear.solon.test.SolonTest;

/**
 *
 * @author noear 2026/3/23 created
 *
 */
@SolonTest
public class Demo87 {
    @Test
    public void main() {
        Dami.bus().send("demo87.event.user", new User(85));
        Dami.bus().send("demo87.event.user-no", new User(85));
    }

    @DamiTopic("demo87.event.user")
    public static class UserListener implements EventListener<User> {

        @Transaction(policy = TranPolicy.not_supported)
        @Override
        public void onEvent(Event<User> event) throws Throwable {
            System.out.println(event.getPayload());
        }
    }
}
