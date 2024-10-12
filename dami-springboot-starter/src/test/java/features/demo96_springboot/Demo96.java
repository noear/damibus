package features.demo96_springboot;


import features.demo96_springboot.orderModule.OrderService;
import features.demo96_springboot.userModule.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami.spring.boot.annotation.DamiScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("features.demo96_springboot")
@DamiScan(basePackages = {"features.demo96_springboot"})
public class Demo96 {
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
