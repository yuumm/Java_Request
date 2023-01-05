package com.cim.request.common.security;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cim.request.common.R;
import com.cim.request.entity.SysMenu;
import com.cim.request.entity.SysRole;
import com.cim.request.entity.SysUser;
import com.cim.request.service.SysMenuService;
import com.cim.request.service.SysRoleService;
import com.cim.request.service.SysUserService;
import com.cim.request.utils.JwtUtils;
import com.cim.request.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
该class配置security中登录成功的情况
对于springsecurity身份验证的流程可以查看：
    https://longda.wang/post/3c7c656a.html
 */

// 后面要注入到securityconfig中 component注解和bean注解类似
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    // 登录成功后返回成功信息并根据用户信息返回token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        // 返回使用outputstream
        ServletOutputStream outputStream = response.getOutputStream();

        // 根据username生成token
        String username = authentication.getName();
        String token = JwtUtils.genJwtToken(username);

        // 根据username获取user的信息
        SysUser currentUser = sysUserService.getByUsername(username);
        // 通过用户id获取所有角色信息
        /*
         inSql表示子查询。其中id表示子查询的内容
         具体的sql语句如下
         select * from sys_role where id in (select role_id from sys_user_role where user_id=)
         */
        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id=" + currentUser.getId()));

        // 遍历所有角色，不重复的获取角色拥有的菜单权限
        /*
        为了保证不重复，所以使用set
        遍历角色，通过角色获取所有的权限，具体的sql语句如下
        select * from sys_menu where id in (select menu_id from sys_role_menu where role_id=1)
         */
        Set<SysMenu> menuSet = new HashSet<>();
        for (SysRole sysRole:roleList) {
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "select menu_id from sys_role_menu where role_id="+sysRole.getId()));
            // 通过遍历来进一步获取菜单权限编码
            for (SysMenu sysMenu:sysMenuList) {
                menuSet.add(sysMenu);
            }
        }

        /*
        获取到用户权限信息之后根据用户权限信息返回左侧菜单栏的内容
         */

        // 将menuset转换为list
        List<SysMenu> sysMenuList = new ArrayList<>(menuSet);

        // 左侧菜单栏是需要排序的，是根据order_num在进行排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));

        // 生成左侧菜单树
        List<SysMenu> menuList = sysMenuService.buildTreeMenu(sysMenuList);
        log.info("menuList : " + menuList);

        // JSONUtil.toJsonStr可以将数据转换为json的字符串
        // 因为stream的数据都是数据流，所以需要getBytes转换为字节
        outputStream.write(JSONUtil.toJsonStr(R.success("登录成功").add("authorization", token).add("currentUser", currentUser).add("menuList", menuList)).getBytes());
        // flush的作用是强制将缓存中的数据发送出去
        outputStream.flush();
        outputStream.close();
    }
}
