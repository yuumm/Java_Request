package com.cim.request.common.security;

import cn.hutool.json.JSONUtil;
import com.cim.request.common.R;
import com.cim.request.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
该class配置security中登录成功的情况
对于springsecurity身份验证的流程可以查看：
    https://longda.wang/post/3c7c656a.html
 */

// 后面要注入到securityconfig中 component注解和bean注解类似
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    // 登录成功后返回成功信息并根据用户信息返回token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        // 返回使用outputstream
        ServletOutputStream outputStream = response.getOutputStream();

        // 根据username生成token
        String username = authentication.getName();
        String token = JwtUtils.genJwtToken(username);

        // JSONUtil.toJsonStr可以将数据转换为json的字符串
        // 因为stream的数据都是数据流，所以需要getBytes转换为字节
        outputStream.write(JSONUtil.toJsonStr(R.success("登录成功").add("authorization", token)).getBytes());
        // flush的作用是强制将缓存中的数据发送出去
        outputStream.flush();
        outputStream.close();
    }
}
