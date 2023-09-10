package demo.userModule.event;

import demo.baseModule.model.User;
import org.noear.dami.solon.annotation.DamiTopic;

//用户需求监听器
@DamiTopic("demo.user")
public class UserDemandListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
