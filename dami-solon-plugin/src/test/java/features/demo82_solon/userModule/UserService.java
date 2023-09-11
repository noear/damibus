package features.demo82_solon.userModule;

import features.demo82_solon.userModule.event.UserEventSender;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

@Component
public class UserService {
    @Inject
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
