package cn.chiayhon.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SpringAopAspect {

    @Pointcut("@annotation(cn.chiayhon.spring.aop.CustomAnnotation)")
    public void pointCut() {
        // do nothing
    }

/*
    @AfterThrowing(pointcut = "pointCut()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("捕获到异常：{}",e.getMessage());
        log.info("注解信息：{}", ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(CustomAnnotation.class).message());
    }
*/

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            log.info("捕获到异常：{}", e.getMessage());
            log.info("注解信息：{}", ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(CustomAnnotation.class).message());
        }
        return null;
    }

}
