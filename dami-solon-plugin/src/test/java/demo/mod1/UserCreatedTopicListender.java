package demo.mod1;

import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.Dami;

import java.util.Map;

@Dami(topicMapping = "demo.user.created")
public class UserCreatedTopicListender implements TopicListener<Payload<Map<String,Object>,Long>> {

    @Override
    public void onEvent(Payload<Map<String, Object>, Long> payload) throws Throwable {
        System.out.println(payload);
        System.out.println("订阅：UserCreatedTopicListender:userId=" + payload.getContent().get("userId"));
    }
}
