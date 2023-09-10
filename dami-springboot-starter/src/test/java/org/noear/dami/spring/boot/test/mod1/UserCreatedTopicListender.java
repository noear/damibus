package org.noear.dami.spring.boot.test.mod1;

import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
@DamiTopic(topicMapping = "demo.user.created")
public class UserCreatedTopicListender implements TopicListener<Payload<Map<String,Object>,Long>> {

    @Override
    public void onEvent(Payload<Map<String, Object>, Long> payload) {
        System.out.println(payload);
        System.out.println("订阅：UserCreatedTopicListender:userId=" + payload.getContent().get("userId"));
    }
}
