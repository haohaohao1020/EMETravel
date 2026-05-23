package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Menu;
import com.emei.tourism.entity.Role;
import com.emei.tourism.mapper.MenuMapper;
import com.emei.tourism.service.MenuService;
import com.emei.tourism.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<Menu> getMenuTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus, 1);
        wrapper.orderByAsc(Menu::getSort);
        List<Menu> allMenus = list(wrapper);
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<Menu> getMenuList() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        return list(wrapper);
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        Role role = roleService.getById(roleId);
        if (role == null || role.getMenuIds() == null || role.getMenuIds().isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> menuIdList = Arrays.stream(role.getMenuIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Menu::getId, menuIdList);
        wrapper.eq(Menu::getStatus, 1);
        wrapper.eq(Menu::getType, 2);
        wrapper.orderByAsc(Menu::getSort);
        List<Menu> menus = list(wrapper);

        return buildTree(menus, 0L);
    }

    @Override
    public List<String> getPermissionsByRoleId(Long roleId) {
        Role role = roleService.getById(roleId);
        if (role == null || role.getMenuIds() == null || role.getMenuIds().isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> menuIdList = Arrays.stream(role.getMenuIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Menu::getId, menuIdList);
        wrapper.eq(Menu::getStatus, 1);
        wrapper.isNotNull(Menu::getPermission);
        List<Menu> menus = list(wrapper);

        return menus.stream()
                .map(Menu::getPermission)
                .filter(p -> p != null && !p.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean createMenu(Menu menu) {
        return save(menu);
    }

    @Override
    public boolean updateMenu(Menu menu) {
        return updateById(menu);
    }

    @Override
    public boolean deleteMenu(Long id) {
        return removeById(id);
    }

    private List<Menu> buildTree(List<Menu> menus, Long parentId) {
        List<Menu> tree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                menu.setChildren(buildTree(menus, menu.getId()));
                tree.add(menu);
            }
        }
        return tree;
    }
}
