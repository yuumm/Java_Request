package com.cim.request.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entity.SysMenu;
import com.cim.request.entity.SysRequest;
import com.cim.request.entity.SysRole;
import com.cim.request.entity.SysUser;
import com.cim.request.mapper.SysMenuMapper;
import com.cim.request.mapper.SysRoleMapper;
import com.cim.request.mapper.SysRoleMenuMapper;
import com.cim.request.mapper.SysUserMapper;
import com.cim.request.service.SysUserService;
import com.cim.request.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // mybatis测试（只需要修改service，controller，mapper即可）
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    public List<SysUser> getUserId() {
        return sysUserMapper.getUserId();
    }

    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        // 因为要涉及到拼接，所以要用StringBuffer
        StringBuffer authority = new StringBuffer();

        // 通过用户id获取所有角色信息
        /*
         inSql表示子查询。其中id表示子查询的内容
         具体的sql语句如下
         select * from sys_role where id in (select role_id from sys_user_role where user_id=)
         */
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id="+userId));
        // 这里进行拼接，拼接的格式和MyUserDetailsServiceImpl中的要求一样
        if (roleList.size() > 0) {
            /*
            通过Stream流进行遍历，r是每个元素，通过collect返回对应的字符串
             */
            String roleCodeStrs = roleList.stream().map(r->"ROLE_"+r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStrs);
        }

        // 遍历所有角色，不重复的获取角色拥有的菜单权限
        /*
        为了保证不重复，所以使用set
        遍历角色，通过角色获取所有的权限，具体的sql语句如下
        select * from sys_menu where id in (select menu_id from sys_role_menu where role_id=1)
         */
        Set<String> menuCodeSet = new HashSet<>();
        for (SysRole sysRole:roleList) {
            List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id", "select menu_id from sys_role_menu where role_id="+sysRole.getId()));
            // 通过遍历来进一步获取菜单权限编码
            for (SysMenu sysMenu:sysMenuList) {
                // perms就是权限编码
                String perms = sysMenu.getPerms();
                if (StringUtil.isNotEmpty(perms)) {
                    menuCodeSet.add(perms);
                }
            }
        }
        // 进行拼接，拼接要求和上述要求一样
        if (menuCodeSet.size() > 0) {
            authority.append(",");
            // 依然通过stream流进行遍历，此时不需要特殊处理，因此不用map
            String menuCodeStrs = menuCodeSet.stream().collect(Collectors.joining(","));
            authority.append(menuCodeStrs);
        }

        System.out.println("authority: " + authority.toString());

        return authority.toString();
    }
}
