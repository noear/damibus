package demo82_solon.orderModule;

import demo82_solon.baseModule.model.User;
import demo82_solon.orderModule.event.OrderEventSender;
import demo82_solon.orderModule.event.UserDemandSender;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

@Component
public class OrderService {
    @Inject
    UserDemandSender userDemandSender;
    @Inject
    OrderEventSender orderEventSender;

    public void addOrder(long userId) {
        //获取用户
        User user = userDemandSender.getUser(userId);
        System.err.println("Order:User:getUser: " + user);

        //创建订单
        long orderId = System.currentTimeMillis();

        //发送事件
        orderEventSender.onCreated(orderId);
    }
}
