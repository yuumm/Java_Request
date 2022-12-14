package com.cim.request.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cim.request.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // mybatis测试
//    @Select("select * from user where id=#{id}")
    public List<SysUser> getUserId();
}
