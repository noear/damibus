package features.demo82_solon;

import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，用户服务接口（要向别人拿的）
 * */
@DamiTopic("demo82.event.user")
public interface EventUserService {
    User getUser(long userId); //方法的主题 = topicMapping + "." + method.getName() //方法不能重名
}
