package demo82_solon;

import demo82_solon.orderModule.OrderService;
import demo82_solon.userModule.UserService;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;

@Component
public class Demo82 {
    @Inject
    UserService userService;

    @Inject
    OrderService orderService;

    @Init
    public void test(){
        /**
         * 场景描述：
         * 1.添加用户；互动那边会有监听打印
         * 2.修改用户；互动那边会有监听打印
         * 3.添加订单；会获取用户（通过事件）；互动那边会有监听打印
         * */

        userService.addUser("noear");
        userService.updateUser(999, "dami");

        orderService.addOrder(1010);
    }

    public static void main(String[] args) {
        Solon.start(Demo82.class, args);
    }
}
