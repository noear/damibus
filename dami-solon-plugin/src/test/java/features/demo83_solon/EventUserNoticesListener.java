package features.demo83_solon;

import org.noear.dami2.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户公告监听（获得别人的消息）
 * */
@DamiTopic("demo83.event.user")
public class EventUserNoticesListener {
    public void onCreated(long userId, String name) {
        System.err.println("1-onCreated: userId=" +userId);
    }
}
