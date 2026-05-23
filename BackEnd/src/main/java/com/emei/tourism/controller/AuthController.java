package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Admin;
import com.emei.tourism.entity.Menu;
import com.emei.tourism.service.AdminService;
import com.emei.tourism.service.MenuService;
import com.emei.tourism.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        
        if (phone == null || phone.length() != 11) {
            return Result.error("请输入正确的手机号");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("请输入密码");
        }
        
        Map<String, Object> result = userService.login(phone, password);
        if (result == null) {
            return Result.error("手机号或密码错误");
        }
        return Result.success("登录成功", result);
    }

    @PostMapping("/admin/login")
    @Operation(summary = "管理员登录")
    public Result<Map<String, Object>> adminLogin(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        if (username == null || username.isEmpty()) {
            return Result.error("请输入用户名");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("请输入密码");
        }
        
        Map<String, Object> result = adminService.login(username, password);
        if (result == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success("登录成功", result);
    }

    @GetMapping("/userInfo")
    @Operation(summary = "获取管理员信息")
    public Result<Map<String, Object>> getAdminInfo(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("adminId");
        if (adminId == null) {
            return Result.unauthorized();
        }
        
        Admin admin = adminService.getAdminInfo(adminId);
        if (admin == null) {
            return Result.error("用户不存在");
        }

        List<Menu> menus = menuService.getMenusByRoleId(admin.getRoleId());
        List<String> permissions = menuService.getPermissionsByRoleId(admin.getRoleId());

        Map<String, Object> result = new HashMap<>();
        result.put("user", admin);
        result.put("menus", menus);
        result.put("permissions", permissions);
        
        return Result.success(result);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }
}
