package features.demo96_springboot.userModule;


import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户广播（要告知别人的）
 * */
@DamiTopic("event.user2")
public interface EventUserBroadcast {
    void onCreated(long userId, String name);
    void onUpdated(long userId, String name);
}
