package org.noear.dami;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 事件装载
 *
 * @author noear
 * @since 1.0
 */
public class Payload implements Serializable {
    private String guid;
    private String topic;
    private String content;

    protected transient CompletableFuture<String> future;


    public Payload(String topic, String content) {
        this.guid = UUID.randomUUID().toString();
        this.topic = topic;
        this.content = content;
    }

    public boolean isRequest(){
        return future != null;
    }

    /**
     * 唯一性id
     * */
    public String getGuid() {
        return guid;
    }

    /**
     * 主题
     * */
    public String getTopic() {
        return topic;
    }

    /**
     * 内容
     * */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "guid='" + guid + '\'' +
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
