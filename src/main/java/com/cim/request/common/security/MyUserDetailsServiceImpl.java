package com.cim.request.common.security;

import com.cim.request.common.exception.UserCountLockException;
import com.cim.request.entity.SysUser;
import com.cim.request.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

// springsecurity中要通过查库的实现登录验证，就需要先实现这个service
// 然后还需要到Securityconfig中进行配置

@Service
@Slf4j
public class MyUserDetailsServiceImpl implements UserDetailsService {

    // 因为要查用户表，因此要用到UserService
    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名错误！");
        } else if ("1".equals(sysUser.getStatus())) {
            throw new UserCountLockException("用户账号已禁用，具体请联系管理员");
        }

        log.info("password-------: {}", sysUser.getPassword());

        return new User(sysUser.getUsername(), sysUser.getPassword(), getUserAuthority());
    }

    private List<GrantedAuthority> getUserAuthority() {
        return new ArrayList<>();
    }
}
