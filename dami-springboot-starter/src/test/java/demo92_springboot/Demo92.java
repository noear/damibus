package demo92_springboot;


import demo92_springboot.orderModule.OrderService;
import demo92_springboot.userModule.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Demo92 {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @EventListener
    public void test(ContextRefreshedEvent event){
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
        SpringApplication.run(Demo92.class);
    }
}
