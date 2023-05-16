package cn.chiayhon.spring.transation;

import cn.chiayhon.DemoTestSupport;
import net.qiyuesuo.sdk.SDKClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class AfterCommitDemoTest extends DemoTestSupport {

    @Autowired
    private AfterCommitExceptionDemo afterCommitDemo;
    @Autowired
    private AfterCommitExceptionDemo2 afterCommitExceptionDemo2;

    @Test
    public void afterCommitDemoTest(){
        afterCommitDemo.test();
    }

    @Test
    public void afterCommitDemo2Test(){
        afterCommitExceptionDemo2.test();
        new SDKClient()
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}