package com.cim.request.common.security;

import com.cim.request.common.exception.UserCountLockException;
import com.cim.request.entity.User;
import com.cim.request.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// springsecurity中要通过查库的实现登录验证，就需要先实现这个service
// 然后还需要到Securityconfig中进行配置

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    // 因为要查用户表，因此要用到UserService
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        } else if ("1".equals(user.getStatus())) {
            throw new UserCountLockException("用户账号已禁用，具体请联系管理员");
        }

        return null;
    }
}
