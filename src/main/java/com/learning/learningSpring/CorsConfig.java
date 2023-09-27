package com.learning.learningSpring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class CorsConfig implements WebMvcConfigurer {

    @Override

    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/forum/**") // Replace with the actual URL pattern of your API

            .allowedOrigins("http://localhost:4200") // Allow requests from your Angular app's origin

            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

            .allowedHeaders("*");

    }

}