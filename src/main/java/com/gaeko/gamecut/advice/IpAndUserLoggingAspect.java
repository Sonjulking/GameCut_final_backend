package com.gaeko.gamecut.advice;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class IpAndUserLoggingAspect {

    private final HttpServletRequest request;

    private static final Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    public IpAndUserLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @After("execution(* com.gaeko..*Controller.*(..))")
    public void logIpAndUsername(JoinPoint joinPoint) {
        String ip = request.getRemoteAddr();
        String username = "anonymous";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            }
        }

        MDC.put("clientIp", ip);
        MDC.put("username", username);
        accessLogger.info("사용자 접속 - IP: {}, 사용자: {}, 메서드: {}", ip, username, joinPoint.getSignature().toShortString());
    }
}