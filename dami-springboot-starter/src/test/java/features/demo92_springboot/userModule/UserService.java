package features.demo92_springboot.userModule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    EventUserBroadcast eventUserBroadcast;

    public void addUser(String name){
        //记录用户
        long userId = System.currentTimeMillis();

        //发送事件
        eventUserBroadcast.onCreated(userId, name);
    }

    public void updateUser(long userId, String name){
        eventUserBroadcast.onUpdated(userId, name);
    }
}
