package features.demo95_springboot;


import features.demo95_springboot.orderModule.OrderService;
import features.demo95_springboot.userModule.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootTest(classes = Demo92.class)
@ComponentScan("features.demo95_springboot")
public class Demo92 {
    @Autowired
    UserService userService;

    @Autowired
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
