package features.demo85_solon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.dami.Dami;
import org.noear.dami.bus.plus.TopicContentListener;
import org.noear.solon.annotation.Component;
import org.noear.solon.test.SolonJUnit5Extension;

//@ExtendWith(SolonJUnit5Extension.class)
public class Demo85 {
    //@Test
    public void main() {
        Dami.busTyped().send(new User(85));
    }

    @Component
    public static class UserListener implements TopicContentListener<User> {
        @Override
        public void onEventContent(User content) throws Throwable {
            System.err.println(content);
        }
    }
}
