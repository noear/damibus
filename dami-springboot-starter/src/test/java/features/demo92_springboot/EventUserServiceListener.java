package features.demo92_springboot;


import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("demo91.event.user")
public class EventUserServiceListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
