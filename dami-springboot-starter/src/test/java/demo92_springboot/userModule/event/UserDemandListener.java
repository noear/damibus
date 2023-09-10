package demo92_springboot.userModule.event;


import demo92_springboot.baseModule.model.User;
import org.noear.dami.spring.boot.annotation.DamiTopic;

//用户需求监听器
@DamiTopic("demo.user")
public class UserDemandListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}
