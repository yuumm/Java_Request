package com.cim.request.common.exception;

// 全局异常处理

import com.cim.request.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

// RestController.class表示捕获含有RestController注解的controller中的异常
@ControllerAdvice(annotations = {RestController.class, Controller.class})
// 后面需要返回json数据，需要该注解
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            // 错误内容是：Duplicate entry 'zhangsan' for key 'employee.idx_username'
            // 我们要获取"zhangsan"这个数据用于返回给前端，那么就可以通过空格进行分隔，然后取出这个内容
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
