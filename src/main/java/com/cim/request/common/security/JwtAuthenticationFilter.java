package com.cim.request.common.security;

import com.cim.request.common.constant.JwtConstant;
import com.cim.request.entity.CheckResult;
import com.cim.request.entity.SysUser;
import com.cim.request.service.SysUserService;
import com.cim.request.utils.JwtUtils;
import com.cim.request.utils.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
使用Springsecurity之后，页面跳转要进行认证，否则前端就会返回302错误
此时的认证需要用到Jwt来，并在Springsecurity中进行配置
*/

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    // 这里就是dofilterinternal方法中说的要用到的范型
    private static final String URL_WHITELIST[] = {
            "/login",
            "/logout",
            "/captcha",
            "/password",
            "/image/**",
            "/test/**"
    };
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    /*
    通过request获取token，然后对这个token进行验证
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 需要创建范型让后面一个filter去进行认证
        String token = request.getHeader("token");
        log.info("请求地址：" + request.getRequestURI());
        log.info("token: " + token);

        // 当token为空或者URL_WHITELIST中包含了url，则直接放行
        if(StringUtil.isEmpty(token) || new ArrayList<String>(Arrays.asList(URL_WHITELIST)).contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 当token不为空的时候进行验证
        CheckResult checkResult = JwtUtils.validateJWT(token);
        // 判断验证失败的情况（自带了三种情况，可以从checkresult的源码中查看）
        if (!checkResult.isSuccess()) {
            switch (checkResult.getErrCode()) {
                case JwtConstant.JWT_ERRCODE_NULL: throw new JwtException("token不存在");
                case JwtConstant.JWT_ERRCODE_FAIL: throw new JwtException("token验证失败");
                case JwtConstant.JWT_ERRCODE_EXPIRE: throw new JwtException("token过期");
            }
        }

        // 解析token并获取信息
        Claims claims = JwtUtils.parseJWT(token);
        // 获取用户的username
        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUsername(username);

        // 将获取到的username保存到security中的上下文
        // 第一个参数是获取的username，第三个参数是权限信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, myUserDetailsService.getUserAuthority(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }
}
