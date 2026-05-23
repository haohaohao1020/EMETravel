package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.FinanceStat;
import com.emei.tourism.service.FinanceStatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@Tag(name = "财务统计接口")
public class FinanceController {

    @Autowired
    private FinanceStatService financeStatService;

    @GetMapping("/daily")
    @Operation(summary = "获取日统计数据")
    public Result<FinanceStat> getDailyStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        FinanceStat result = financeStatService.getDailyStat(date);
        return Result.success(result);
    }

    @GetMapping("/daily/list")
    @Operation(summary = "获取日统计列表")
    public Result<List<FinanceStat>> getDailyStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<FinanceStat> result = financeStatService.getDailyStats(startDate, endDate);
        return Result.success(result);
    }

    @GetMapping("/monthly/list")
    @Operation(summary = "获取月统计列表")
    public Result<List<FinanceStat>> getMonthlyStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<FinanceStat> result = financeStatService.getMonthlyStats(startDate, endDate);
        return Result.success(result);
    }

    @GetMapping("/yearly/list")
    @Operation(summary = "获取年统计列表")
    public Result<List<FinanceStat>> getYearlyStats(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        List<FinanceStat> result = financeStatService.getYearlyStats(startYear, endYear);
        return Result.success(result);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取营收统计")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = Map.of(
                "totalRevenue", financeStatService.getTotalRevenue(startDate, endDate),
                "totalOrders", financeStatService.getTotalOrders(startDate, endDate),
                "totalVisitors", financeStatService.getTotalVisitors(startDate, endDate)
        );
        return Result.success(result);
    }

    @GetMapping("/report")
    @Operation(summary = "获取财务报表")
    public Result<List<FinanceStat>> getReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer type) {
        List<FinanceStat> result;
        if (type == 1) {
            result = financeStatService.getDailyStats(startDate, endDate);
        } else if (type == 2) {
            result = financeStatService.getMonthlyStats(startDate, endDate);
        } else {
            result = financeStatService.getYearlyStats(startDate.getYear(), endDate.getYear());
        }
        return Result.success(result);
    }
}
