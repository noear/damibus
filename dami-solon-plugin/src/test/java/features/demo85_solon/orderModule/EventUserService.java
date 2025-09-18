package features.demo85_solon.orderModule;

import features.demo85_solon.baseModule.User;
import org.noear.dami2.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户服务（要向别人拿的）
 * */
@DamiTopic("event.user")
public interface EventUserService {
    User getUser(long userId);
}
