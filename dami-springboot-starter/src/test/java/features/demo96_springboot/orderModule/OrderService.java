package features.demo96_springboot.orderModule;


import features.demo96_springboot.baseModule.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    @Autowired
    EventUserService eventUserService;
    @Autowired
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
