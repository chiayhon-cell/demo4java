package cn.chiayhon;

import cn.chiayhon.rest.RestControllerDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        context.start();
        final RestControllerDemo bean = context.getBean(RestControllerDemo.class);
        System.out.println("RestControllerDemoBean ==============> " + bean);
    }
}
