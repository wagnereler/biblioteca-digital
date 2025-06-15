package com.wagner.biblioteca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")               // para todas as rotas
                .allowedOriginPatterns("*")      // libera todas as origens
                .allowedMethods("*")             // libera todos os métodos (GET, POST, PUT…)
                .allowedHeaders("*")             // libera todos os headers
                .allowCredentials(true);         // mantém cookies e auth se precisar
    }
}
