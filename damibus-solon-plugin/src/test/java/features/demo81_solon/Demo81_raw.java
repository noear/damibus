package features.demo81_solon;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.TopicListener;
import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo81_raw {
    @Test
    public void main() throws Throwable {
        System.out.println(Dami.bus().send("user.demo", new CallPayload<>("solon"))
                .getPayload()
                .getReceiver()
                .get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements TopicListener<CallPayload<String, String>> {
        @Override
        public void onEvent(Event<CallPayload<String, String>> event) throws Throwable {
            event.getPayload()
                    .getReceiver()
                    .complete("Hi " + event.getPayload().getContent());
        }
    }
}
