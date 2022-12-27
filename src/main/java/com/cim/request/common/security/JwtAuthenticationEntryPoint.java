package com.cim.request.common.security;

import cn.hutool.json.JSONUtil;
import com.cim.request.common.R;
import com.cim.request.utils.JwtUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
该类用来解决出现jwt认证失败的情况，例如token比对失败等
需要到Springsecurity中进行配置
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        // 返回使用outputstream
        ServletOutputStream outputStream = response.getOutputStream();


        // JSONUtil.toJsonStr可以将数据转换为json的字符串
        // 因为stream的数据都是数据流，所以需要getBytes转换为字节
        outputStream.write(JSONUtil.toJsonStr(R.error("认证失败，请先登录")).getBytes());
        // flush的作用是强制将缓存中的数据发送出去
        outputStream.flush();
        outputStream.close();
    }
}
