package features.demo93_springboot;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户公告监听（获得别人的消息）
 * */
@DamiTopic(value = "demo93.event.user", index = 2) //可以控制监听顺序
public class EventUserNoticesListener2 { // implements EventUserNotices
    public void onCreated(long userId, String name) {
        System.err.println("2-onCreated: userId=" +userId);
    }
}
