package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Ticket;
import com.emei.tourism.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket")
@Tag(name = "票务接口")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/list")
    @Operation(summary = "获取票务列表")
    public Result<List<Ticket>> getTicketList(@RequestParam(required = false) Integer type) {
        List<Ticket> list = ticketService.getTicketList(type);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取票务详情")
    public Result<Ticket> getTicketDetail(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketDetail(id);
        if (ticket == null) {
            return Result.error("票务不存在");
        }
        return Result.success(ticket);
    }

    @GetMapping("/stock")
    @Operation(summary = "获取库存信息")
    public Result<Map<String, Object>> getStockInfo(
            @RequestParam Long ticketId,
            @RequestParam(required = false) String date) {
        LocalDate useDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Map<String, Object> stockInfo = ticketService.getStockInfo(ticketId, useDate);
        return Result.success(stockInfo);
    }

    @GetMapping("/timeSlots")
    @Operation(summary = "获取可预约时段")
    public Result<List<String>> getTimeSlots(
            @RequestParam Long ticketId,
            @RequestParam(required = false) String date) {
        LocalDate useDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        List<String> slots = ticketService.getTimeSlots(ticketId, useDate);
        return Result.success(slots);
    }
}
