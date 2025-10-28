package com.sdu_ai_lab.project_tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:avatars/");

        registry.addResourceHandler("/cv/**")
                .addResourceLocations("file:cv/");
    }
}
