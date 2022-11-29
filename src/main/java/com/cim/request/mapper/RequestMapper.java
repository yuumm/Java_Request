package com.cim.request.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cim.request.entiy.Request;
import com.cim.request.entiy.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RequestMapper extends BaseMapper<Request> {
}
