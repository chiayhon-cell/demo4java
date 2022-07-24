package cn.chiayhon.spring.base;

import cn.chiayhon.demo.ApplicationEventDemo;
import cn.chiayhon.demo.DatabaseInitDemo;
import cn.chiayhon.demo.SpringTransactionControlDemo;
import cn.chiayhon.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Initialization for system
 */
@Component
@Slf4j
public class ApplicationRunnerDemo implements ApplicationRunner {
    @Resource
    private ApplicationEventDemo applicationEventDemo;
    @Resource
    private SpringTransactionControlDemo springTransactionControlDemo;
    @Resource
    private DatabaseInitDemo databaseInitDemo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=========== 【start runner initialization】 ===========");
        init();
        log.info("=========== 【end runner initialization】 ===========");
    }

    private void init() {
        applicationEventDemoTest(false);
        springTransactionControlDemoTest(false);
        databaseInitTest(false);
    }


    public void databaseInitTest(boolean enable) {
        if (enable) {
            log.info("===== databaseInitTest start =====");
            new Thread(() -> databaseInitDemo.main()).start();
            log.info("===== databaseInitTest start =====");
        }

    }

    /**
     * test for springTransactionControlDemo {@link SpringTransactionControlDemo}
     */
    public void springTransactionControlDemoTest(boolean enable) {
        if (enable) {
            log.info("===== springTransactionControlDemoTest start =====");
            new Thread(() -> springTransactionControlDemo.main()).start();
            log.info("===== springTransactionControlDemoTest start =====");
        }
    }

    /**
     * test for ApplicationEvent {@link org.springframework.context.ApplicationEvent}
     */
    private void applicationEventDemoTest(boolean enable) {
        if (enable) {
            log.info("===== eventPublisherTest start =====");
            final User user = new User();
            user.setUsername("彭于晏");
            user.setPassword("1234");
            final boolean success = applicationEventDemo.register(user);
            String message = success ? "注册完成" : "用户已存在";
            log.info("注册结果:" + message);
            log.info("===== eventPublisherTest end =====");
        }
    }


}
