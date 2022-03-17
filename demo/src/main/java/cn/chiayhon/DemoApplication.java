package cn.chiayhon;

import cn.chiayhon.spring.aop.SpringAopTest;
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
        springAopTest(context);
    }

    private static void springAopTest(ConfigurableApplicationContext context) {
        final SpringAopTest testBean = context.getBean(SpringAopTest.class);
        testBean.aopTest(4);
        testBean.aopTest(1);
    }
}
