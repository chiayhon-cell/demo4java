package cn.chiayhon.spring.transation;

import cn.chiayhon.DemoTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

class AfterCommitFixDemo3Test extends DemoTestSupport {

    @Autowired
    private AfterCommitFixDemo3 afterCommitFixDemo3;

    @Test
    void test1() {
        afterCommitFixDemo3.test();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}