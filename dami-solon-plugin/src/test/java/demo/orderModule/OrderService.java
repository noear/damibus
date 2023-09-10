package demo.orderModule;

import demo.baseModule.model.User;
import demo.orderModule.event.OrderEventSender;
import demo.orderModule.event.UserDemandSender;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;

@Component
public class OrderService {
    @Inject
    UserDemandSender userDemandSender;
    @Inject
    OrderEventSender orderEventSender;

    @Tran
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
