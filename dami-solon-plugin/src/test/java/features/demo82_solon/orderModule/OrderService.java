package features.demo82_solon.orderModule;

import features.demo82_solon.baseModule.User;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

@Component
public class OrderService {
    @Inject
    EventUserService eventUserService;
    @Inject
    EventOrderBroadcast eventOrderBroadcast;

    public long addOrder(long userId) {
        //获取用户
        User user = eventUserService.getUser(userId);
        System.err.println("Order:User:getUser: " + user);

        //创建订单
        long orderId = System.currentTimeMillis();

        //发送事件
        eventOrderBroadcast.onCreated(orderId);

        return user.getUserId() * 10;
    }
}
