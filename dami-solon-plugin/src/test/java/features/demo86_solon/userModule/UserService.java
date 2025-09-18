package features.demo86_solon.userModule;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

@Component
public class UserService {
    @Inject
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
