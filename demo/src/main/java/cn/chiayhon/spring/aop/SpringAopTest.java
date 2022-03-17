package cn.chiayhon.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringAopTest {

    @CustomAnnotation(message = "测试信息")
    public void aopTest(int num) {
        if (num < 2) {
            log.error("测试方法异常,参数:{}", num);
//            throw new UnknownException("运行时异常");
        }
        log.info("测试方法正常执行,参数:{}", num);
    }
}
