package com.gaeko.gamecut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:8002/upload/파일명 으로 접근 가능하게 설정
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:./upload/");
    }
}
