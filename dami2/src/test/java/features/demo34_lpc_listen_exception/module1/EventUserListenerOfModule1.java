package features.demo34_lpc_listen_exception.module1;

import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.receivable.CallEventListener;
import org.noear.dami2.bus.receivable.CallPayload;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
 * */
public class EventUserListenerOfModule1 implements CallEventListener<Map,Void> {
    //public void onCreated(Long userId, String name) { }

    @Override
    public void onCall(Event<CallPayload<Map, Void>> event, Map data, CompletableFuture<Void> sink) {
        Long userId = (Long) data.get("userId");
        String name = (String) data.get("name");

        System.err.println("onCreated: userId=" + userId + ", name=" + name);
        sink.completeExceptionally(new RuntimeException("测试异常"));
    }
}