
package com.gaeko.gamecut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//TODO : 추후에, spring security에서 설정해야될수도...
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로 허용
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173") // 프론트 주소
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false); // 쿠키 전달 안 하면 false
    }
}
