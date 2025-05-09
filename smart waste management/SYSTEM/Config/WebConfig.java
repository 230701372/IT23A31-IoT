package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Config;


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
                registry.addMapping("/api/**") // Allow all endpoints
                        .allowedOrigins("*") // Flutter web dev server
                        .allowedMethods("*") // GET, POST, etc.
                        .allowedHeaders("*");// Allow all headers
            }
        };
    }
}
