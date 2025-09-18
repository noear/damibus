package features.demo83_solon;

import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户公告（让别人知道）
 * */
@DamiTopic("demo82.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}
