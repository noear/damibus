package features.demo17_monitor;

import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.noear.dami2.bus.EventDispatcherDefault;

import java.util.List;

/**
 * @author noear 2023/10/13 created
 */
public class TopicDispatcherMonitor extends EventDispatcherDefault {
    @Override
    protected void doDistribute(Event event, List<EventListenerHolder> targets) throws Throwable {
        //开始监视...
        System.out.println("开始监视...");

        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            EventListener<Event> listener = targets.get(i).getListener();

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
