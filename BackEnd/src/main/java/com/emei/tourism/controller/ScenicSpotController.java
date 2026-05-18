package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.ScenicSpot;
import com.emei.tourism.service.ScenicSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenic")
@Tag(name = "景点接口")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @GetMapping("/list")
    @Operation(summary = "获取景点列表")
    public Result<List<ScenicSpot>> getSpotList(
            @RequestParam(required = false) String keyword) {
        List<ScenicSpot> list = scenicSpotService.getSpotList(keyword);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取景点详情")
    public Result<ScenicSpot> getSpotDetail(@PathVariable Long id) {
        ScenicSpot spot = scenicSpotService.getSpotDetail(id);
        if (spot == null) {
            return Result.error("景点不存在");
        }
        return Result.success(spot);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐景点")
    public Result<List<ScenicSpot>> getRecommendSpots() {
        List<ScenicSpot> list = scenicSpotService.getRecommendSpots();
        return Result.success(list);
    }
}
