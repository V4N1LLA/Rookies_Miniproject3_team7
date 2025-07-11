package com.basic.myspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://moodiary_frontend", "http://frontend")  // 프론트엔드 주소
                        .allowedMethods("*")  // GET, POST, PUT, DELETE, OPTIONS 등 허용
                        .allowedHeaders("*")  // 모든 헤더 허용
                        .allowCredentials(true);  // 쿠키/토큰 등 포함 허용
            }
        };
    }
}
