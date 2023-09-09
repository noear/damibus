package org.noear.dami.api;

import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Payload;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author noear
 * @since 1.0
 */
public class MethodTopicListener implements TopicListener<Payload<Object,Object>> {
    private DamiBus bus;
    private Object target;
    private Method method;
    private Coder coder;

    public MethodTopicListener(DamiBus bus, Object target, Method method, Coder coder) {
        this.bus = bus;
        this.target = target;
        this.method = method;
        this.coder = coder;
    }

    @Override
    public void onEvent(Payload payload) throws Throwable {
        //解码
        Object[] args = coder.decode(method, payload.getContent());

        //执行
        Object rst = method.invoke(target, args);

        if (payload.isRequest()) {
            //响应
            if (rst instanceof CompletableFuture) {
                //响应式回调
                ((CompletableFuture) rst).thenAccept(rst2 -> {
                    bus.response(payload, rst2);
                });
            } else if (rst instanceof Future) {
                //等待回调
                Object rst2 = ((Future) rst).get();
                bus.response(payload, rst2);
            } else {
                //返回
                bus.response(payload, rst);
            }
        }
    }
}