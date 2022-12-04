package com.cim.request.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cim.request.common.R;
import com.cim.request.entity.Request;
import com.cim.request.service.RequestService;
import com.cim.request.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

//    需求分页查询。参数中page表示查询第几页的数据，pagesize表示每页多少条数据
    @GetMapping("/page")
//    RequestHeader表示数据从header中获取，required表示该参数不是必须的
    public R<Page> page(@RequestHeader(required = false)String token, int page, int pageSize) {
        log.info("page: {}, pageSize: {}", page, pageSize);
//        增加token判断登录信息
        if(StringUtil.isEmpty(token)) {
            return R.error("没有权限访问");
        }
        log.info("token: {}", token);

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

    // 根据id查询详细信息
    @GetMapping("/{id}")
    public R<Request> getRequestDetail(@PathVariable Long id) {
        log.info("根据id查询信息 {}", id);
        LambdaQueryWrapper<Request> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Request::getId, id);
        Request request = requestService.getOne(queryWrapper);
        if (request == null) {
            return R.error("查询失败");
        }
        return R.success(request);
    }

    // 根据id修改信息
    @PutMapping
    public R<String> updateRequestById(@RequestBody Request request) {
        Long requestId = (Long) request.getId();
        log.info("requestId {}", requestId);

        requestService.updateById(request);
        return R.success("信息编辑成功");
    }

    @PostMapping("/save")
    public  R<String> saveRequest(@RequestBody Request request) {
        log.info("save request {}", request.getBackground());
        requestService.save(request);
        return R.success("添加成功");
    }

//    @PutMapping("/{id}")
//    public R<String> deleteRequestById(@PathVariable Long id) {
//        log.info("根据id删除信息 {}", id);
//
//        LambdaQueryWrapper<Request> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(Request::getId, id);
//
//        return null;
//    }
}
