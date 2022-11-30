package com.cim.request.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cim.request.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // mybatis测试
//    @Select("select * from user where id=#{id}")
    public List<User> getUserId();
}
