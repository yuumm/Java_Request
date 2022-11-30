package com.cim.request.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cim.request.entity.Request;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestMapper extends BaseMapper<Request> {
}
