package features.demo95_springboot;

import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.InterceptorChain;
import org.noear.dami.bus.Event;
import org.springframework.stereotype.Component;

@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Event event, InterceptorChain chain) {
        System.out.println("拦截：" + event);
        chain.doIntercept(event);
    }
}
