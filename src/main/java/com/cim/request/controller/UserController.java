package com.cim.request.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cim.request.common.R;
import com.cim.request.entiy.User;
import com.cim.request.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    // 主要接收json格式，RequestBody可以将json格式的数据进行对应
    // 因为登录用户要保存部分数据到session以便查询是否登录，所以需要HttpServletRequest
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        // 1、密码进行md5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2、根据username到数据库中进行查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User usr = userService.getOne(queryWrapper);

        // 3、查询失败直接返回失败
        if (usr == null) {
            return R.error("用户不存在");
        }

        // 4、密码比对，不一致时返回失败
        if (!usr.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        // 5、查看员工状态，status为0时返回失败
        if (usr.getStatus() == 0) {
            return R.error("员工已禁用");
        }

        // 6、登录成功，将员工id存入session
        request.getSession().setAttribute("user", usr.getId());

        return R.success(usr);
    }

    @PostMapping("/register")
    public R<String> register(HttpServletRequest request, @RequestBody User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        userService.save(user);

        return R.success("新增员工成功");
    }

    //mybatis测试
    @GetMapping("/getUser/{id}")
    public R<User> getUserId(@PathVariable Long id) {
        User user = userService.getUserId(id);

        return R.success(user);
    }
}
