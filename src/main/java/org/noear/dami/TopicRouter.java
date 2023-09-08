package org.noear.dami;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 主题路由器
 *
 * @author noear
 * @since 1.0
 */
public class TopicRouter {

    //===========
    private Map<String, TopicListenPipeline<Payload>> pipelineMap = new LinkedHashMap<>();

    /**
     * 添加监听
     */
    public void add(String topic, TopicListener<Payload> listener) {
        add(topic, 0, listener);
    }

    /**
     * 添加监听
     */
    public void add(String topic, int index, TopicListener<Payload> listener) {
        assertTopic(topic);

        TopicListenPipeline<Payload> pipeline = pipelineMap.get(topic);
        if (pipeline == null) {
            pipeline = new TopicListenPipeline<>();
            pipelineMap.put(topic, pipeline);
        }

        pipeline.add(index, listener);
    }

    /**
     * 移除监听
     */
    public void remove(String topic, TopicListener<Payload> listener) {
        assertTopic(topic);

        TopicListenPipeline<Payload> pipeline = pipelineMap.get(topic);
        if (pipeline != null) {
            pipeline.remove(listener);
        }
    }


    /**
     * 接收事件并路由
     */
    public void handle(Payload payload) {
        assertTopic(payload.getTopic());

        TopicListenPipeline<Payload> pipeline = pipelineMap.get(payload.getTopic());
        if (pipeline != null) {
            try {
                pipeline.onEvent(payload);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void assertTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("The topic cannot be empty");
        }
    }
}
