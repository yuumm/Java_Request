package com.cim.request.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cim.request.common.R;
import com.cim.request.entiy.Request;
import com.cim.request.service.RequestService;
import com.cim.request.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

//    需求分页查询。参数中page表示查询第几页的数据，pagesize表示每页多少条数据
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String query) {
        log.info("page: {}, pageSize: {}", page, pageSize);

        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Request> queryWrapper = new LambdaQueryWrapper();
//        if (query != null) {
//            queryWrapper.like(Request::getId, query);
//        }
        queryWrapper.orderByDesc(Request::getCreateTime);

        requestService.page(pageInfo, queryWrapper);
        log.info("pageInfo: {}", pageInfo);
        return R.success(pageInfo);
    }

}
