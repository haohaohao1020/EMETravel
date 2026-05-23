package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    
    List<Menu> getMenuTree();
    
    List<Menu> getMenuList();
    
    List<Menu> getMenusByRoleId(Long roleId);
    
    List<String> getPermissionsByRoleId(Long roleId);
    
    boolean createMenu(Menu menu);
    
    boolean updateMenu(Menu menu);
    
    boolean deleteMenu(Long id);
}
