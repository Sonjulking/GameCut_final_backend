package com.gaeko.gamecut.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
    //@AfterThrowing : 메서드 실행 "후에"
    //예외가 발생한 경우에만 동작
    @AfterThrowing(pointcut = "execution(* com.gaeko..*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("[Exception] {} 에서 예외 발생: {}", methodName, ex.getMessage(), ex);
    }
}
