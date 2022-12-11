package com.cim.request.common.security;

import cn.hutool.json.JSONUtil;
import com.cim.request.common.R;
import com.cim.request.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
该class配置security中登录成功的情况
 */
// 后面要注入到securityconfig中 component注解和bean注解类似
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        // 返回使用outputstream
        ServletOutputStream outputStream = response.getOutputStream();

        String message = exception.getMessage();
        if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误";
        }

        // JSONUtil.toJsonStr可以将数据转换为json的字符串
        // 因为stream的数据都是数据流，所以需要getBytes转换为字节。因为内容中有中文，因此指定utf-8编码
        outputStream.write(JSONUtil.toJsonStr(R.error(message)).getBytes("UTF-8"));
        // flush的作用是强制将缓存中的数据发送出去
        outputStream.flush();
        outputStream.close();
    }
}
