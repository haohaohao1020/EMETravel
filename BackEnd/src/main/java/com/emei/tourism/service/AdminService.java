package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Admin;

import java.util.Map;

public interface AdminService extends IService<Admin> {
    
    Map<String, Object> login(String username, String password);
    
    Admin getAdminInfo(Long adminId);
    
    Page<Admin> getAdminList(Integer page, Integer size, String username, Integer status);
    
    boolean createAdmin(Admin admin);
    
    boolean updateAdmin(Admin admin);
    
    boolean deleteAdmin(Long id);
    
    boolean resetPassword(Long id, String password);
    
    boolean toggleStatus(Long id, Integer status);
}
