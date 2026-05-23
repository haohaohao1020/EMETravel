package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.TicketStock;
import com.emei.tourism.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticketStock")
@Tag(name = "库存接口")
public class TicketStockController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/list")
    @Operation(summary = "获取库存列表")
    public Result<Page<TicketStock>> getTicketStockList(
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<TicketStock> stockPage = ticketService.getTicketStockList(ticketId, startDate, endDate, page, size);
        return Result.success(stockPage);
    }

    @PutMapping
    @Operation(summary = "更新库存")
    public Result<Void> updateTicketStock(@RequestBody TicketStock stock) {
        boolean success = ticketService.updateTicketStock(stock);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @PutMapping("/{id}/lock")
    @Operation(summary = "锁定库存")
    public Result<Void> lockTicketStock(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer lockedStock = params.get("lockedStock");
        boolean success = ticketService.lockTicketStock(id, lockedStock);
        if (!success) {
            return Result.error("锁定失败");
        }
        return Result.success("锁定成功", null);
    }

    @GetMapping("/info")
    @Operation(summary = "获取库存信息")
    public Result<Map<String, Object>> getStockInfo(
            @RequestParam Long ticketId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        LocalDate useDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Map<String, Object> stockInfo = ticketService.getStockInfo(ticketId, useDate);
        return Result.success(stockInfo);
    }

    @GetMapping("/timeSlots")
    @Operation(summary = "获取可预约时段")
    public Result<List<String>> getTimeSlots(
            @RequestParam Long ticketId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        LocalDate useDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        List<String> slots = ticketService.getTimeSlots(ticketId, useDate);
        return Result.success(slots);
    }
}
