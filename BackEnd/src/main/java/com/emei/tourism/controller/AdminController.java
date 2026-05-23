package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Admin;
import com.emei.tourism.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "管理员接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    @Operation(summary = "获取管理员列表")
    public Result<Page<Admin>> getAdminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        Page<Admin> result = adminService.getAdminList(page, size, username, status);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增管理员")
    public Result<Void> createAdmin(@RequestBody Admin admin) {
        boolean success = adminService.createAdmin(admin);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新管理员")
    public Result<Void> updateAdmin(@RequestBody Admin admin) {
        boolean success = adminService.updateAdmin(admin);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除管理员")
    public Result<Void> deleteAdmin(@PathVariable Long id) {
        boolean success = adminService.deleteAdmin(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/resetPassword")
    @Operation(summary = "重置密码")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String password = params.get("password");
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        boolean success = adminService.resetPassword(id, password);
        if (!success) {
            return Result.error("重置密码失败");
        }
        return Result.success("重置密码成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换状态")
    public Result<Void> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        boolean success = adminService.toggleStatus(id, status);
        if (!success) {
            return Result.error("状态切换失败");
        }
        return Result.success("状态切换成功", null);
    }
}
