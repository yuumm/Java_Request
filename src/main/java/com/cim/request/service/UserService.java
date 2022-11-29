package com.cim.request.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cim.request.entiy.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends IService<User> {
    // mybatis测试
    public User getUserId(Long id);
}
