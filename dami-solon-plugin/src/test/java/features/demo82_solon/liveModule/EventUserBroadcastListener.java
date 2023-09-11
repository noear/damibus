package features.demo82_solon.liveModule;

import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户广播监听器
 * */
@DamiTopic("event.user")
public class EventUserBroadcastListener  {

    public void onCreated(long userId, String name) {
        System.err.println("Live:User:onCreated: userId=" + userId + ", name=" + name);
    }
    public void onUpdated(long userId, String name) {
        System.err.println("Live:User:onUpdated: userId=" + userId + ", name=" + name);
    }
}
