package demo92_springboot.orderModule.event;


import demo92_springboot.baseModule.model.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("demo.user")
public interface UserDemandSender {
    User getUser(long userId);
}
