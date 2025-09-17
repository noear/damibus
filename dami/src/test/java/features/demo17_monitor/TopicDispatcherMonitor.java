package features.demo17_monitor;

import org.noear.dami.bus.Event;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.impl.TopicDispatcherDefault;

import java.util.List;

/**
 * @author noear 2023/10/13 created
 */
public class TopicDispatcherMonitor extends TopicDispatcherDefault {
    @Override
    protected void doDispatch(Event event, List<TopicListenerHolder> targets) throws Throwable {
        //开始监视...
        System.out.println("开始监视...");

        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            TopicListener<Event> listener = targets.get(i).getListener();

            //发送前监视...
            System.out.println("发送前监视...");
            listener.onEvent(event);
            //发送后监视...
            System.out.println("发送后监视...");
        }

        //结速监视...
        System.out.println("结速监视...");
    }
}
