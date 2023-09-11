package features.demo92_springboot.userModule.event;


import features.demo92_springboot.baseModule.model.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

//用户需求监听器
@DamiTopic("demo.user")
@Component
public class UserDemandListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
