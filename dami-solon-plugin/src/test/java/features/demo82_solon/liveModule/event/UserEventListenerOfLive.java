package features.demo82_solon.liveModule.event;

import org.noear.dami.solon.annotation.DamiTopic;

//保持与 Sender 相同的接口定义
//用户事件监听器
@DamiTopic("demo.user")
public class UserEventListenerOfLive {
    public void onCreated(long userId, String name) {
        System.err.println("LIve:User:onCreated: userId=" + userId + ", name=" + name);
    }
    public void onUpdated(long userId, String name) {
        System.err.println("LIve:User:onUpdated: userId=" + userId + ", name=" + name);
    }
}
