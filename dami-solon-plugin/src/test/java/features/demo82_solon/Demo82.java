package features.demo82_solon;

import features.demo82_solon.orderModule.OrderService;
import features.demo82_solon.userModule.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

@RunWith(SolonJUnit4ClassRunner.class)
public class Demo82 {
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
