package demo92_solon.userModule;

import demo92_solon.userModule.event.UserEventSender;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;

@Component
public class UserService {
    @Inject
    UserEventSender userEventSender;

    @Tran
    public void addUser(String name){
        //记录用户
        long userId = System.currentTimeMillis();

        //发送事件
        userEventSender.onCreated(userId, name);
    }

    @Tran
    public void updateUser(long userId, String name){
        userEventSender.onUpdated(userId, name);
    }
}
