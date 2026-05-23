package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Ticket;
import com.emei.tourism.entity.TicketStock;
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
    @Operation(summary = "获取票种列表")
    public Result<Page<Ticket>> getTicketList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Ticket> ticketPage = ticketService.getAdminTicketList(name, type, status, page, size);
        return Result.success(ticketPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取票种详情")
    public Result<Ticket> getTicketDetail(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketDetail(id);
        if (ticket == null) {
            return Result.error("票种不存在");
        }
        return Result.success(ticket);
    }

    @PostMapping
    @Operation(summary = "新增票种")
    public Result<Void> createTicket(@RequestBody Ticket ticket) {
        boolean success = ticketService.createTicket(ticket);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新票种")
    public Result<Void> updateTicket(@RequestBody Ticket ticket) {
        boolean success = ticketService.updateTicket(ticket);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除票种")
    public Result<Void> deleteTicket(@PathVariable Long id) {
        boolean success = ticketService.deleteTicket(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换票种状态")
    public Result<Void> toggleTicketStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        boolean success = ticketService.toggleTicketStatus(id, status);
        if (!success) {
            return Result.error("状态切换失败");
        }
        return Result.success("状态切换成功", null);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有票种")
    public Result<List<Ticket>> getAllTickets(@RequestParam(required = false) Integer type) {
        List<Ticket> list = ticketService.getTicketList(type);
        return Result.success(list);
    }
}
