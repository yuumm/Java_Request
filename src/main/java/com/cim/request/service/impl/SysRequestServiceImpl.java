package com.cim.request.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entity.SysRequest;
import com.cim.request.mapper.SysRequestMapper;
import com.cim.request.service.SysRequestService;
import org.springframework.stereotype.Service;

@Service
public class SysRequestServiceImpl extends ServiceImpl<SysRequestMapper, SysRequest> implements SysRequestService {
}
