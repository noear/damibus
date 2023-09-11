package features.demo92_springboot.userModule;


import features.demo92_springboot.userModule.event.UserEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    UserEventSender userEventSender;

    public void addUser(String name){
        //记录用户
        long userId = System.currentTimeMillis();

        //发送事件
        userEventSender.onCreated(userId, name);
    }

    public void updateUser(long userId, String name){
        userEventSender.onUpdated(userId, name);
    }
}
