package features.demo82_solon.userModule.event;

import features.demo82_solon.baseModule.model.User;
import org.noear.dami.solon.annotation.DamiTopic;

//用户需求监听器
@DamiTopic("demo.user")
public class UserDemandListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
