package features.demo81_solon;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.Event;
import org.noear.dami.bus.receivable.CallPayload;
import org.noear.dami.bus.receivable.CallTopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.test.SolonTest;

import java.util.concurrent.CompletableFuture;

@SolonTest
public class Demo81 {
    @Test
    public void main() throws Throwable {
        System.out.println(Dami.bus().call("user.demo", "solon").get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements CallTopicListener<String, String> {
        @Override
        public void onCall(Event<CallPayload<String, String>> event, String content, CompletableFuture<String> receiver) {
            receiver.complete("Hi " + content);
        }
    }
}
