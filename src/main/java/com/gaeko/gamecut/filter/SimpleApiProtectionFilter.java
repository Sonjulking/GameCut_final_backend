// 2025년 7월 14일 수정됨 - 간단한 API 보호 필터
package com.gaeko.gamecut.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Order(1) // 가장 먼저 실행되도록 설정
public class SimpleApiProtectionFilter implements Filter {

    private final List<String> allowedReferers = Arrays.asList(
        "http://localhost:5173",
        "http://3.37.238.85",
        "http://www.gamecut.net",
        "http://gamecut.net"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        
        // API 경로가 아니면 통과
        if (!requestURI.startsWith("/api/") || 
            requestURI.startsWith("/api/user/login") ||
            requestURI.startsWith("/api/user/join") ||
            "OPTIONS".equals(method)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Referer 검증
        String referer = httpRequest.getHeader("Referer");
        if (referer != null && allowedReferers.stream().anyMatch(referer::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 차단
        log.warn("API 직접 접근 차단 - Referer: {}, URI: {}", referer, requestURI);
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"error\": \"Access denied\"}");
    }
}