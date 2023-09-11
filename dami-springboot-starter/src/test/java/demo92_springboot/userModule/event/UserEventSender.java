package demo92_springboot.userModule.event;


import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

/**
 * 提示：需要要支持类隔离的环境，请使用基本类型做为参数与返回
 * */
//用户事件发送器
@DamiTopic("demo.user")
@Component
public interface UserEventSender {
    void onCreated(long userId, String name);
    void onUpdated(long userId, String name);
}
