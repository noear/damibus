package features.demo82_solon;

import org.noear.dami.solon.annotation.DamiTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于事件的，用户服务实现（要让别人来拿的）
 * */
//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("demo82.event.user")
public class EventUserServiceImpl { // implements EventUserService // 它相当于是个实现类
    static final Logger log = LoggerFactory.getLogger(EventUserServiceImpl.class);

    public User getUser(long userId) {
        log.debug("userId={}", userId);
        return new User(userId);
    }
}
