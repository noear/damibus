package features.demo95_springboot.userModule;


import org.noear.dami2.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户广播（要告知别人的）
 * */
@DamiTopic("event.user")
public interface EventUserBroadcast {
    void onCreated(long userId, String name);
    void onUpdated(long userId, String name);
}
