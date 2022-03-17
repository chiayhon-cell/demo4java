package cn.chiayhon.spring.aop;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomAnnotation {

    // 测试信息
    String message();
}
