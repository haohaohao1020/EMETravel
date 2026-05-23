package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Role;
import com.emei.tourism.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/role")
@Tag(name = "角色接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @Operation(summary = "获取角色列表")
    public Result<Page<Role>> getRoleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        Page<Role> result = roleService.getRoleList(page, size, name);
        return Result.success(result);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有角色")
    public Result<List<Role>> getAllRoles() {
        List<Role> result = roleService.getAllRoles();
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public Result<Void> createRole(@RequestBody Role role) {
        boolean success = roleService.createRole(role);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public Result<Void> updateRole(@RequestBody Role role) {
        boolean success = roleService.updateRole(role);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> deleteRole(@PathVariable Long id) {
        boolean success = roleService.deleteRole(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }
}
