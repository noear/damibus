package features.demo96_springboot;

import org.noear.dami2.bus.intercept.EventInterceptor;
import org.noear.dami2.bus.intercept.InterceptorChain;
import org.noear.dami2.bus.Event;
import org.springframework.stereotype.Component;

@Component
public class DamiInterceptorImpl implements EventInterceptor {
    @Override
    public void doIntercept(Event event, InterceptorChain chain) {
        System.out.println("拦截：" + event);
        chain.doIntercept(event);
    }
}
