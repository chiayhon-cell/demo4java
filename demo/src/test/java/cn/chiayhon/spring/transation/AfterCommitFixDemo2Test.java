package cn.chiayhon.spring.transation;

import cn.chiayhon.DemoTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AfterCommitFixDemo2Test extends DemoTestSupport {

    @Autowired
    private AfterCommitFixDemo2 afterCommitFixDemo2;

    @Test
    void test1() {
        afterCommitFixDemo2.test();
    }
}