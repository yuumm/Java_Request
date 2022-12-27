package com.cim.request.config;

import com.cim.request.common.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/*
spring security配置
 */
@Configuration
@EnableWebSecurity
// 全局的方法配置，后期要用到对应的security的注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 用于实现查库的登录验证
    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    // 导入了密码加密的设置。必须导入，否则会报错
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String URL_WHITELIST[] = {
            "/login",
            "/logout",
            "/captcha",
            "/password",
            "/image/**",
            "/test/**"
    };

    // 配置jwt
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 实现自己配置登录（查库登录）登出功能
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启跨域配置 以及关闭csrf攻击
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable()

        // 登录登出配置
        .formLogin()
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
//        .and().logout().logoutSuccessHandler()

        // session禁用配置（因为是前后端分离，所以不用session）
        // STATELESS表示无状态
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // 拦截规则配置
        // URL_WHITELIST白名单 是一个数组
        .and().authorizeRequests().antMatchers(URL_WHITELIST).permitAll()
        // 其他需求需要认证
        .anyRequest().authenticated()

        // 异常处理配置
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)

        // 自定义过滤器配置(例如jwt的认证)
            // 在security默认认证之前添加jwt认证
        .and()
        .addFilter(jwtAuthenticationFilter());
    }

    CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}
