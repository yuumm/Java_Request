package com.cim.request.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entiy.User;
import com.cim.request.mapper.UserMapper;
import com.cim.request.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // mybatis测试（只需要修改service，controller，mapper即可）
    @Autowired
    private UserMapper userMapper;
    public User getUserId(Long id) {
        return userMapper.getUserId(id);
    }
}
