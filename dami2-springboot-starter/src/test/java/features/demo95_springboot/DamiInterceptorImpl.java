package features.demo95_springboot;

import org.noear.dami2.bus.Interceptor;
import org.noear.dami2.bus.InterceptorChain;
import org.noear.dami2.bus.Event;
import org.springframework.stereotype.Component;

@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Event event, InterceptorChain chain) {
        System.out.println("拦截：" + event);
        chain.doIntercept(event);
    }
}
