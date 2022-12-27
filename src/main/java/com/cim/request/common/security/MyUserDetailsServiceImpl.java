package com.cim.request.common.security;

import com.cim.request.common.exception.UserCountLockException;
import com.cim.request.entity.SysUser;
import com.cim.request.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
            throw new UsernameNotFoundException("用户名错误或密码错误！");
        } else if (sysUser.getStatus() == 0) {
            throw new UserCountLockException("用户账号已禁用，具体请联系管理员");
        }

        log.info("password-------: {}", sysUser.getPassword());

        return new User(sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
    }

    // 权限信息设置
    public List<GrantedAuthority> getUserAuthority(Long userId) {

        // 获取用户的Authority
        String authority = sysUserService.getUserAuthorityInfo(userId);

        /* 通过向下述方法传递一个字符串，可以返回一个List类
       具体的格式如下：
       ROLE_admin,ROLE_common,system:user:resetPwd,system:role:delete,system:user:list,
       system:menu:query,system:menu:list,system:menu:add,system:user:delete,system:role:list,
       system:role:menu,system:user:edit,system:user:query,system:role:edit,system:user:add,
       system:user:role,system:menu:delete,system:role:add,system:role:query,system:menu:edit
         */
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
