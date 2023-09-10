package org.noear.dami.spring.boot.test.mod1;


import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

//保持与 Sender 相同的接口定义
@DamiTopic(topicMapping = "demo.user")
@Component
public class UserEventListenerImpl {
    public long created(long userId, String name) {
        System.out.println("created: userId=" + userId + ", name=" + name);
        return userId;
    }
    public void updated(long userId, String name) {
        System.out.println("updated: userId=" + userId + ", name=" + name);
    }
}
