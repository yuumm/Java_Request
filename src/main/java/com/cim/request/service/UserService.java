package com.cim.request.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cim.request.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    // mybatis测试
    public List<User> getUserId();

    public User getByUsername(String username);
}
