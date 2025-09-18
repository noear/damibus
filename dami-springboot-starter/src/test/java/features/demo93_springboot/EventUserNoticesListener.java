package features.demo93_springboot;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户公告监听（获得别人的消息）
 * */
@DamiTopic("demo93.event.user")
public class EventUserNoticesListener {
    public void onCreated(long userId, String name) {
        System.err.println("1-onCreated: userId=" +userId);
    }
}
