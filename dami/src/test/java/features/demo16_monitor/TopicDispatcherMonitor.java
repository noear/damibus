package features.demo16_monitor;

import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.impl.TopicDispatcherDefault;

import java.util.List;

/**
 * @author noear 2023/10/13 created
 */
public class TopicDispatcherMonitor<P> extends TopicDispatcherDefault<P> {
    @Override
    protected void doDispatch(Message<P> message, List<TopicListenerHolder<P>> targets) throws Throwable {
        //开始监视...
        System.out.println("开始监视...");

        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            TopicListener<Message<P>> listener = targets.get(i).getListener();

            //发送前监视...
            System.out.println("发送前监视...");
            listener.onEvent(message);
            //发送后监视...
            System.out.println("发送后监视...");
        }

        //结速监视...
        System.out.println("结速监视...");
    }
}
