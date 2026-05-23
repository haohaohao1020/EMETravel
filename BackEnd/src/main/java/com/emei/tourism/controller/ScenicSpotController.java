package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.ScenicSpot;
import com.emei.tourism.service.ScenicSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenicSpot")
@Tag(name = "景点接口")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @GetMapping("/list")
    @Operation(summary = "获取景点列表")
    public Result<Page<ScenicSpot>> getScenicSpotList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ScenicSpot> spotPage = scenicSpotService.getAdminSpotList(name, status, page, size);
        return Result.success(spotPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取景点详情")
    public Result<ScenicSpot> getScenicSpot(@PathVariable Long id) {
        ScenicSpot spot = scenicSpotService.getSpotDetail(id);
        if (spot == null) {
            return Result.error("景点不存在");
        }
        return Result.success(spot);
    }

    @PostMapping
    @Operation(summary = "新增景点")
    public Result<Void> createScenicSpot(@RequestBody ScenicSpot spot) {
        boolean success = scenicSpotService.createSpot(spot);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新景点")
    public Result<Void> updateScenicSpot(@RequestBody ScenicSpot spot) {
        boolean success = scenicSpotService.updateSpot(spot);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除景点")
    public Result<Void> deleteScenicSpot(@PathVariable Long id) {
        boolean success = scenicSpotService.deleteSpot(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换景点状态")
    public Result<Void> toggleScenicSpotStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        boolean success = scenicSpotService.toggleSpotStatus(id, status);
        if (!success) {
            return Result.error("状态切换失败");
        }
        return Result.success("状态切换成功", null);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有景点")
    public Result<List<ScenicSpot>> getAllSpots(@RequestParam(required = false) String keyword) {
        List<ScenicSpot> list = scenicSpotService.getSpotList(keyword);
        return Result.success(list);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐景点")
    public Result<List<ScenicSpot>> getRecommendSpots() {
        List<ScenicSpot> list = scenicSpotService.getRecommendSpots();
        return Result.success(list);
    }
}
