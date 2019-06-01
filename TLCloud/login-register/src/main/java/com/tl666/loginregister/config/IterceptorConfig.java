package com.tl666.loginregister.config;

import com.tl666.loginregister.iterceptor.RequestLimitIterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IterceptorConfig implements WebMvcConfigurer {
    @Autowired
    RequestLimitIterceptor requestLimitIterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(requestLimitIterceptor).addPathPatterns("/**");

    }

}
