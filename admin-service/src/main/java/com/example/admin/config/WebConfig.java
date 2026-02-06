package com.example.admin.config;

import com.example.admin.interceptor.LoginCheckInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**") // 모든 경로에 대해 검사
                .excludePathPatterns( // 단, 아래 경로는 검사 제외 (로그인 페이지 등)
                        "/", "/login", "/api/admins", "/api/check-login", "/api/login", "/api/logout",
                        "/css/**", "/*.ico", "/error"
                );
    }

}
