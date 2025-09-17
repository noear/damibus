package features.demo80_solon;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Event;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.payload.RequestPayload;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo80 {
    @Test
    public void main() throws Throwable {
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
            event.getPayload()
                    .getReceiver()
                    .complete("Hi " + event.getPayload().getContext());
        }
    }
}
