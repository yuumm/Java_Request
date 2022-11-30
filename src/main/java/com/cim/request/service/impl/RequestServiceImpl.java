package com.cim.request.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entity.Request;
import com.cim.request.mapper.RequestMapper;
import com.cim.request.service.RequestService;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl extends ServiceImpl<RequestMapper, Request> implements RequestService {
}
