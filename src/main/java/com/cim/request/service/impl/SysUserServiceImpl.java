package com.cim.request.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entity.SysUser;
import com.cim.request.mapper.SysUserMapper;
import com.cim.request.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // mybatis测试（只需要修改service，controller，mapper即可）
    @Autowired
    private SysUserMapper sysUserMapper;
    public List<SysUser> getUserId() {
        return sysUserMapper.getUserId();
    }

    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }
}
