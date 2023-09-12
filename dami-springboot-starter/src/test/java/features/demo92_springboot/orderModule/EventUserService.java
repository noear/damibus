package features.demo92_springboot.orderModule;


import features.demo92_springboot.baseModule.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务（要向别人拿的）
 * */
@DamiTopic("event.user")
public interface EventUserService {
    User getUser(long userId);
}
