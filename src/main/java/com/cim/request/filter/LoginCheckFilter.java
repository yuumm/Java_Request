package com.cim.request.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//检查用户是否完成登录(验证登录功能没有用到后端，因为在前端用了路由守卫来判断)
// filterName表示filter的名字，urlPatterns表示要拦截哪些路径请求，这里是所有请求都要拦截
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 强转为HttpServletRequest方便获取URL
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1、获取请求的url
        String requestURI = request.getRequestURI();

        // 2、判断本次请求是否需要处理
        String[] urls = new String[] {
                "/user/login",
                "/user/logout"
        };
        boolean check = check(urls, requestURI);

        // 3、如果不需要处理，则直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4、判断登录状体，如果已经登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            filterChain.doFilter(request, response);
            return;
        }


        log.info("拦截到请求：{}", request.getRequestURI());
        // 通过FilterChain放行
        filterChain.doFilter(request, response);
    }

    // 匹配路径，判断是否可以放行
    public boolean check(String[] urls, String requestURI) {
        for (String url: urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
