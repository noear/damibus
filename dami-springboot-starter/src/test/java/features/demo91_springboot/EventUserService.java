package features.demo91_springboot;


import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务（要向别人拿的）
 * */
@DamiTopic("demo91.event.user")
public interface EventUserService {
    User getUser(long userId);
}
