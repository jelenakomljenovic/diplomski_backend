package com.example.university.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private final static long MAX_AGE_SECS = 3600;

  @Value("${app.cors.allowedOrigins}")
  private String[] allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods(CorsConfiguration.ALL)
        .allowedHeaders(CorsConfiguration.ALL)
        .exposedHeaders(CorsConfiguration.ALL)
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }
}
