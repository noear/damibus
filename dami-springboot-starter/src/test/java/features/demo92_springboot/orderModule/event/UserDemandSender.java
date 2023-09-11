package features.demo92_springboot.orderModule.event;


import features.demo92_springboot.baseModule.model.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

@DamiTopic("demo.user")
@Component
public interface UserDemandSender {
    User getUser(long userId);
}
