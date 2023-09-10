package demo82_solon;

import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.InterceptorChain;
import org.noear.dami.bus.Payload;
import org.noear.solon.annotation.Component;

@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Payload payload, InterceptorChain chain) {
        System.out.println("拦截：" + payload);
        chain.doIntercept(payload);
    }
}
