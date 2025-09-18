package features.demo83_solon;

import org.noear.dami2.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户公告（让别人知道）
 * */
@DamiTopic("demo83.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}
