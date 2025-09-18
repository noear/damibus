package features.demo82_solon;

import org.noear.dami.solon.annotation.DamiTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
@DamiTopic("demo81.event.user")
public class EventUserServiceListener {
    static final Logger log = LoggerFactory.getLogger(EventUserServiceListener.class);

    public User getUser(long userId) {
        log.debug("userId={}", userId);
        return new User(userId);
    }
}
