package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Route;
import com.emei.tourism.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
@Tag(name = "路线接口")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/list")
    @Operation(summary = "获取路线列表")
    public Result<Page<Route>> getRouteList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        Page<Route> result = routeService.getRouteList(page, size, name, status);
        return Result.success(result);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有路线")
    public Result<List<Route>> getAllRoutes() {
        List<Route> result = routeService.getAllRoutes();
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取路线详情")
    public Result<Route> getRouteDetail(@PathVariable Long id) {
        Route result = routeService.getRouteDetail(id);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增路线")
    public Result<Void> createRoute(@RequestBody Route route) {
        boolean success = routeService.createRoute(route);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新路线")
    public Result<Void> updateRoute(@RequestBody Route route) {
        boolean success = routeService.updateRoute(route);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除路线")
    public Result<Void> deleteRoute(@PathVariable Long id) {
        boolean success = routeService.deleteRoute(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }
}
