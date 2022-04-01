package cn.chiayhon;

import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        context.start();
        eventPublisherTest(context);
    }

    private static void eventPublisherTest(ConfigurableApplicationContext context) {
        final UserController userController = context.getBean(UserController.class);

        final User user = new User();
        user.setUsername("彭于晏");
        user.setPassword("1234");
        final boolean success = userController.register(user);
        String message = success ? "注册完成" : "用户已存在";
        log.info("注册结果:" + message);
    }

//    private static void springAopTest(ConfigurableApplicationContext context) {
//        final SpringAopTest testBean = context.getBean(SpringAopTest.class);
//    }
}
