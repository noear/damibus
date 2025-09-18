package features.demo92_springboot;


import org.noear.dami2.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，用户服务监听（要让别人来拿的）
 * */
//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("demo92.event.user")
public class EventUserServiceImpl { // implements EventUserService // 它相当于是个实现类
    public User getUser(long userId) {
        return new User(userId);
    }
}
