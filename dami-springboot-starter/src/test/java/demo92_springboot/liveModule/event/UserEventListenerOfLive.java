package demo92_springboot.liveModule.event;

import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

//保持与 Sender 相同的接口定义
//用户事件监听器
@DamiTopic("demo.user")
@Component
public class UserEventListenerOfLive {
    public void onCreated(long userId, String name) {
        System.err.println("LIve:User:onCreated: userId=" + userId + ", name=" + name);
    }
    public void onUpdated(long userId, String name) {
        System.err.println("LIve:User:onUpdated: userId=" + userId + ", name=" + name);
    }
}
