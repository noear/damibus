package features.demo86_solon.userModule;

import features.demo86_solon.baseModule.User;
import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("event.user")
public class EventUserServiceListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
