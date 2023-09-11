package features.demo82_solon.orderModule;

import features.demo82_solon.baseModule.User;
import org.noear.dami.solon.annotation.DamiTopic;

@DamiTopic("demo.user")
public interface UserDemandSender {
    User getUser(long userId);
}
