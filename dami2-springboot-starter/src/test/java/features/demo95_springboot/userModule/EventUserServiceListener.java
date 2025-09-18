package features.demo95_springboot.userModule;


import features.demo95_springboot.baseModule.User;
import org.noear.dami2.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("event.user")
public class EventUserServiceListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
