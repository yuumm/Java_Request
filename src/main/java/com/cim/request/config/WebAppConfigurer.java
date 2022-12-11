package com.cim.request.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web项目配置类
 */
@Configuration
public class WebAppConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")// 对所有请求路径
//                        .allowedOrigins("*")// 允许所有域名
                        .allowedOriginPatterns("*")// 允许所有域名
                        .allowCredentials(true)// 允许cookie等凭证
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")// 允许所有方法
                        .maxAge(3600);
            }
        };
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/image/userAvatar/**").addResourceLocations("file:D:\\java1234-admin3\\userAvatar\\");
//    }


}
