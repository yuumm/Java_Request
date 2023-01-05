package com.cim.request.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cim.request.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
    // mybatis测试
    public List<SysUser> getUserId();

    public SysUser getByUsername(String username);

    public String getUserAuthorityInfo(Long userId);
}
