package com.appointment.management.pact.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LayoutCOnfig implements WebMvcConfigurer {

    @Autowired
    private  AppInterceptor appInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
