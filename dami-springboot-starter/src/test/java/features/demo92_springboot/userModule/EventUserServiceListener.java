package features.demo92_springboot.userModule;


import features.demo92_springboot.baseModule.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("event.user")
@Component
public class EventUserServiceListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
