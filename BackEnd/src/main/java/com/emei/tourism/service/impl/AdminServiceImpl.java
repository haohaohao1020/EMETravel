package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Admin;
import com.emei.tourism.entity.Role;
import com.emei.tourism.mapper.AdminMapper;
import com.emei.tourism.service.AdminService;
import com.emei.tourism.service.RoleService;
import com.emei.tourism.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        Admin admin = getOne(wrapper);

        if (admin == null) {
            return null;
        }

        if (admin.getStatus() != 1) {
            return null;
        }

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(admin.getPassword())) {
            return null;
        }

        admin.setLastLoginTime(LocalDateTime.now());
        updateById(admin);

        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername());

        Role role = roleService.getById(admin.getRoleId());
        admin.setRoleName(role != null ? role.getName() : "");
        admin.setPassword(null);
        admin.setToken(token);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("adminInfo", admin);
        return result;
    }

    @Override
    public Admin getAdminInfo(Long adminId) {
        Admin admin = getById(adminId);
        if (admin != null) {
            Role role = roleService.getById(admin.getRoleId());
            admin.setRoleName(role != null ? role.getName() : "");
            admin.setPassword(null);
        }
        return admin;
    }

    @Override
    public Page<Admin> getAdminList(Integer page, Integer size, String username, Integer status) {
        Page<Admin> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(Admin::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(Admin::getStatus, status);
        }
        wrapper.orderByDesc(Admin::getCreateTime);
        Page<Admin> result = page(pageParam, wrapper);
        for (Admin admin : result.getRecords()) {
            admin.setPassword(null);
            Role role = roleService.getById(admin.getRoleId());
            admin.setRoleName(role != null ? role.getName() : "");
        }
        return result;
    }

    @Override
    public boolean createAdmin(Admin admin) {
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            admin.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        } else {
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        }
        admin.setStatus(1);
        return save(admin);
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        admin.setPassword(null);
        return updateById(admin);
    }

    @Override
    public boolean deleteAdmin(Long id) {
        return removeById(id);
    }

    @Override
    public boolean resetPassword(Long id, String password) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        return updateById(admin);
    }

    @Override
    public boolean toggleStatus(Long id, Integer status) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(status);
        return updateById(admin);
    }
}
