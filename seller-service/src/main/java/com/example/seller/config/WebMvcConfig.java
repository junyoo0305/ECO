package com.example.seller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // [수정] 주소 패턴을 /images/** 에서 /market/images/** 로 변경
        // 이렇게 해야 게이트웨이(8000번)를 통과할 수 있습니다.
        registry.addResourceHandler("/market/images/**")
                .addResourceLocations("file:///C:/eco_images/");
    }
}