package demo82_solon.orderModule.event;

import demo82_solon.baseModule.model.User;
import org.noear.dami.solon.annotation.DamiTopic;

@DamiTopic("demo.user")
public interface UserDemandSender {
    User getUser(long userId);
}
