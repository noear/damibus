package features.demo92_springboot.liveModule;

import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;


/**
 * 基于事件的，用户广播监听器
 * */
@DamiTopic("event.user")
@Component
public class EventUserBroadcastListener  {

    public void onCreated(long userId, String name) {
        System.err.println("Live:User:onCreated: userId=" + userId + ", name=" + name);
    }
    public void onUpdated(long userId, String name) {
        System.err.println("Live:User:onUpdated: userId=" + userId + ", name=" + name);
    }
}
