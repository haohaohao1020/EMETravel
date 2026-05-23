package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    
    Page<Role> getRoleList(Integer page, Integer size, String name);
    
    List<Role> getAllRoles();
    
    boolean createRole(Role role);
    
    boolean updateRole(Role role);
    
    boolean deleteRole(Long id);
}
