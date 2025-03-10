package features.demo96_springboot.userModule;


import features.demo96_springboot.baseModule.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("event.user2")
public class EventUserServiceListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
