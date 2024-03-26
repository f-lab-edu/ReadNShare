package com.flab.readnshare.global.config;

import com.flab.readnshare.global.common.auth.jwt.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/signUp")
                .excludePathPatterns("/signIn")
                .excludePathPatterns("/api/auth/refresh")
                .excludePathPatterns("/api/auth/signIn")
                .excludePathPatterns("/api/member/signUp");
    }

}
