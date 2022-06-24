package cn.chiayhon.spring.base;

import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.ApplicationEvetDemo;
import cn.chiayhon.spring.sql.SpringTransactionControlDemo;
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
    private ApplicationEvetDemo applicationEvetDemo;
    @Resource
    private SpringTransactionControlDemo springTransactionControlDemo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=========== 【start runner initialization】 ===========");
        init();
        log.info("=========== 【end runner initialization】 ===========");
    }

    private void init() {
        applicationEventDemoTest(false);
        springTransactionControlDemoTest(false);
    }


    /**
     * test for springTransactionControlDemo {@link cn.chiayhon.spring.sql.SpringTransactionControlDemo}
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
            final boolean success = applicationEvetDemo.register(user);
            String message = success ? "注册完成" : "用户已存在";
            log.info("注册结果:" + message);
            log.info("===== eventPublisherTest end =====");
        }
    }


}
