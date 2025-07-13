// 2025-07-12 생성됨
package com.gaeko.gamecut.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청 디버깅을 위한 테스트 컨트롤러
 * 실제 요청이 컨트롤러까지 도달하는지 확인용
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class DebugController {

    @GetMapping("/debug/test")
    public Map<String, Object> simpleTest() {
        log.info("=== 디버그 테스트 엔드포인트 호출됨 ===");
        return Map.of(
            "status", "success",
            "message", "API 요청이 성공적으로 도달했습니다!",
            "timestamp", System.currentTimeMillis()
        );
    }

    @GetMapping("/debug/headers")
    public Map<String, Object> checkHeaders(HttpServletRequest request) {
        log.info("=== 헤더 확인 엔드포인트 호출됨 ===");
        
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
            log.info("Header: {} = {}", headerName, headerValue);
        }
        
        return Map.of(
            "status", "success",
            "headers", headers,
            "requestURI", request.getRequestURI(),
            "method", request.getMethod(),
            "remoteAddr", request.getRemoteAddr()
        );
    }

    @GetMapping("/debug/user-check")
    public Map<String, Object> userCheckDebug(@RequestParam(required = false) String userId) {
        log.info("=== 사용자 체크 디버그 엔드포인트 호출됨 ===");
        log.info("받은 userId 파라미터: {}", userId);
        
        return Map.of(
            "status", "success",
            "message", "UserController와 동일한 패턴의 요청이 도달했습니다!",
            "receivedUserId", userId != null ? userId : "null",
            "timestamp", System.currentTimeMillis()
        );
    }

    @PostMapping("/debug/post-test")
    public Map<String, Object> postTest(@RequestBody(required = false) Map<String, Object> body) {
        log.info("=== POST 요청 디버그 엔드포인트 호출됨 ===");
        log.info("받은 body: {}", body);
        
        return Map.of(
            "status", "success",
            "message", "POST 요청이 성공적으로 처리되었습니다!",
            "receivedBody", body != null ? body : "empty",
            "timestamp", System.currentTimeMillis()
        );
    }

    @RequestMapping(value = "/debug/all-methods", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Map<String, Object> allMethods(HttpServletRequest request) {
        log.info("=== 모든 HTTP 메서드 허용 엔드포인트 호출됨 ===");
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Request URI: {}", request.getRequestURI());
        
        return Map.of(
            "status", "success",
            "method", request.getMethod(),
            "uri", request.getRequestURI(),
            "message", "모든 HTTP 메서드가 정상적으로 처리되었습니다!",
            "timestamp", System.currentTimeMillis()
        );
    }
}