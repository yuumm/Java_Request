package com.cim.request.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cim.request.entity.SysMenu;
import com.cim.request.service.SysMenuService;
import com.cim.request.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author liuxiaozheng
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2022-12-25 16:40:47
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    // 该方法用来将左侧菜单栏生成菜单树
    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        List<SysMenu> resultMenuList = new ArrayList<>();
        for (SysMenu sysMenu:sysMenuList) {
            // 查找子节点
            for (SysMenu e:sysMenuList) {
                if (e.getParentId() == sysMenu.getId()) {
                    sysMenu.getChildren().add(e);
                }
            }
            // 如果是父节点直接添加到列表中（父节点的编号是0）
            if (sysMenu.getParentId() == 0L) {
                resultMenuList.add(sysMenu);
            }
        }

        return resultMenuList;
    }
}




