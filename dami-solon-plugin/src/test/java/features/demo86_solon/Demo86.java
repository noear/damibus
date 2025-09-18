package features.demo86_solon;

import features.demo86_solon.orderModule.OrderService;
import features.demo86_solon.userModule.UserService;

import org.junit.jupiter.api.Test;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo86 {
    @Inject
    UserService userService;

    @Inject
    OrderService orderService;

    @Test
    public void main(){
        /**
         * 场景描述：
         * 1.添加用户；互动那边会有监听打印
         * 2.修改用户；互动那边会有监听打印
         * 3.添加订单；会获取用户（通过事件）；互动那边会有监听打印
         * */

        userService.addUser("noear");
        userService.updateUser(999, "dami");

        long orderId = orderService.addOrder(1010);

        assert orderId == 1010 *10;
    }
}
