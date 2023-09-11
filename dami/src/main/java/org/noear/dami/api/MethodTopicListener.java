package org.noear.dami.api;

import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Payload;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 方法主题监听器
 *
 * @author noear
 * @since 1.0
 */
public class MethodTopicListener implements TopicListener<Payload<Object,Object>> {
    private DamiApi damiApi;
    private Object target;
    private Method method;

    public MethodTopicListener(DamiApi damiApi, Object target, Method method) {
        this.damiApi = damiApi;
        this.target = target;
        this.method = method;
    }

    @Override
    public void onEvent(Payload payload) throws Throwable {
        //解码
        Object[] args = damiApi.getCoder().decode(method, payload.getContent());

        //执行
        Object rst = method.invoke(target, args);

        if (payload.isRequest()) {
            //答复
            if (rst instanceof CompletableFuture) {
                //响应式回调
                ((CompletableFuture) rst).thenAccept(rst2 -> {
                    damiApi.getBus().reply(payload, rst2);
                });
            } else if (rst instanceof Future) {
                //等待回调
                Object rst2 = ((Future) rst).get();
                damiApi.getBus().reply(payload, rst2);
            } else {
                //返回
                damiApi.getBus().reply(payload, rst);
            }
        }
    }
}