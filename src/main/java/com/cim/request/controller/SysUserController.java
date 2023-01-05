package com.cim.request.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cim.request.common.R;
import com.cim.request.entity.SysUser;
import com.cim.request.service.SysUserService;
import com.cim.request.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /*
    这是原本的登录的方式，现在使用了springSecurity就不需要再用下述controller了
     */
//    @PostMapping("/login")
//    // 主要接收json格式，RequestBody可以将json格式的数据进行对应
//    // 因为登录用户要保存部分数据到session以便查询是否登录，所以需要HttpServletRequest
//    public R<SysUser> login(HttpServletRequest request, @RequestBody SysUser sysUser) {
//        // 1、密码进行md5加密
//        String password = sysUser.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//
//        // 2、根据username到数据库中进行查询
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
//        SysUser usr = sysUserService.getOne(queryWrapper);
//
//        // 3、查询失败直接返回失败
//        if (usr == null) {
//            return R.error("用户不存在");
//        }
//
//        // 4、密码比对，不一致时返回失败
//        if (!usr.getPassword().equals(password)) {
//            return R.error("密码错误");
//        }
//
//        // 5、查看员工状态，status为0时返回失败
//        if (usr.getStatus() == 0) {
//            return R.error("员工已禁用");
//        }
//
//        // 6、登录成功，生成token，并将token存入session
//        //  根据username生成token
//        String token = JwtUtils.genJwtToken(sysUser.getUsername());
//        //  将token存入session
//        request.getSession().setAttribute("token", token);
//
//        // 返回数据，并将token添加到map中
//        return R.success(usr).add("token", token);
//    }

    // 注册用户
    @PostMapping("/register")
    public R<String> register(HttpServletRequest request, @RequestBody SysUser sysUser) {
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));

        sysUserService.save(sysUser);

        return R.success("新增员工成功");
    }

    //mybatis测试
    @GetMapping("/getUser")
    public R<List<SysUser>> getUserId() {
        List<SysUser> sysUser = sysUserService.getUserId();

        return R.success(sysUser);
    }
}
