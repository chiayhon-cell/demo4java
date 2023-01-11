package cn.chiayhon.spring.transation;

import cn.chiayhon.DemoTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

class AfterCommitFixDemo1Test extends DemoTestSupport {

    @Autowired
    private AfterCommitFixDemo1 afterCommitFixDemo1;

    @Test
    void test1() {
        afterCommitFixDemo1.test();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}