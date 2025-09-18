package features.demo82_solon;

import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户服务（要向别人拿的）
 * */
@DamiTopic("demo81.event.user")
public interface EventUserService {
    User getUser(long userId);
}
