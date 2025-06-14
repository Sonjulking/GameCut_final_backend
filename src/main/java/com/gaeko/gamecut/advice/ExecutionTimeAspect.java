package com.gaeko.gamecut.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    //@Around : 메서드 실행 전 + 후 + 예외 발생까지 모두
    @Around("execution(* com.gaeko..*(..))") // com.gaeko 하위 모든 메서드
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // 실제 메서드 실행

        long end = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[ExecutionTime] {} 실행 시간: {} ms", methodName, (end - start));

        return result;
    }
}
