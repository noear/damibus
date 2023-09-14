package features.demo92_springboot;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户公告（让别人知道）
 * */
@DamiTopic("demo92.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}
