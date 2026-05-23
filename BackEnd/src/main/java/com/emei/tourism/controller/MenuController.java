package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Menu;
import com.emei.tourism.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/menu")
@Tag(name = "菜单接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @Operation(summary = "获取菜单列表")
    public Result<List<Menu>> getMenuList() {
        List<Menu> result = menuService.getMenuList();
        return Result.success(result);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取菜单树")
    public Result<List<Menu>> getMenuTree() {
        List<Menu> result = menuService.getMenuTree();
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    public Result<Void> createMenu(@RequestBody Menu menu) {
        boolean success = menuService.createMenu(menu);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    public Result<Void> updateMenu(@RequestBody Menu menu) {
        boolean success = menuService.updateMenu(menu);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        boolean success = menuService.deleteMenu(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }
}
