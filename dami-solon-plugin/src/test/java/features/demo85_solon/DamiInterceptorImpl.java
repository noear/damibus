package features.demo85_solon;

import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.InterceptorChain;
import org.noear.dami.bus.Event;
import org.noear.solon.annotation.Component;

@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Event event, InterceptorChain chain) {
        System.out.println("拦截：" + event);
        chain.doIntercept(event);
    }
}
