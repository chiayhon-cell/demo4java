package cn.chiayhon.spring.base;

import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.ApplicationEvetDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Initialization for system
 */
@Component
@Slf4j
public class ApplicationRunnerDemo implements ApplicationRunner {
    @Autowired
    private ApplicationEvetDemo applicationEvetDemo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("========== start initialization ==========");
        init();
    }

    private void init() {
        eventPublisherTest();
    }


    /**
     * test for ApplicationEvent {@link org.springframework.context.ApplicationEvent}
     */
    private void eventPublisherTest() {
        log.info("========================== eventPublisherTest start ==========================");
        final User user = new User();
        user.setUsername("彭于晏");
        user.setPassword("1234");
        final boolean success = applicationEvetDemo.register(user);
        String message = success ? "注册完成" : "用户已存在";
        log.info("注册结果:" + message);
        log.info("========================== eventPublisherTest end ==========================");
    }
}
