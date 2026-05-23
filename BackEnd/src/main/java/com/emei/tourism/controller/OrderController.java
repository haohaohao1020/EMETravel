package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Order;
import com.emei.tourism.entity.VerifyRecord;
import com.emei.tourism.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Tag(name = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "获取订单列表")
    public Result<Page<Order>> getOrderList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> orderPage = orderService.getAdminOrderList(orderNo, status, visitorName, startTime, endTime, page, size);
        return Result.success(orderPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        Map<String, Object> detail = orderService.getAdminOrderDetail(id);
        if (detail == null) {
            return Result.error("订单不存在");
        }
        return Result.success(detail);
    }

    @PostMapping("/verify")
    @Operation(summary = "核销票券")
    public Result<Map<String, Object>> verifyTicket(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String code = params.get("code");
        Map<String, Object> result = orderService.verifyTicket(code, operatorId);
        if (result == null) {
            return Result.error("核销失败，核销码无效");
        }
        return Result.success("核销成功", result);
    }

    @GetMapping("/refundList")
    @Operation(summary = "获取退票列表")
    public Result<Page<Order>> getRefundList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> orderPage = orderService.getRefundList(orderNo, status, page, size);
        return Result.success(orderPage);
    }

    @PutMapping("/{id}/approveRefund")
    @Operation(summary = "审核通过退票")
    public Result<Void> approveRefund(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> params,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String remark = params != null ? params.get("remark") : "";
        boolean success = orderService.approveRefund(id, remark, operatorId);
        if (!success) {
            return Result.error("审核失败");
        }
        return Result.success("审核通过", null);
    }

    @PutMapping("/{id}/rejectRefund")
    @Operation(summary = "审核拒绝退票")
    public Result<Void> rejectRefund(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> params,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String remark = params != null ? params.get("remark") : "";
        boolean success = orderService.rejectRefund(id, remark, operatorId);
        if (!success) {
            return Result.error("审核失败");
        }
        return Result.success("审核拒绝", null);
    }

    @GetMapping("/export")
    @Operation(summary = "导出订单")
    public Result<List<Order>> exportOrder(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Order> list = orderService.getOrderListForExport(orderNo, status, visitorName, startTime, endTime);
        return Result.success(list);
    }

    @GetMapping("/verifyRecord")
    @Operation(summary = "获取核销记录")
    public Result<Page<VerifyRecord>> getVerifyRecord(
            @RequestParam(required = false) String verifyCode,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<VerifyRecord> recordPage = orderService.getVerifyRecordList(verifyCode, ticketId, startTime, endTime, page, size);
        return Result.success(recordPage);
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<Map<String, Object>> createOrder(
            @RequestBody Map<String, Object> orderData,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> result = orderService.createOrder(orderData, userId);
        if (result == null) {
            return Result.error("创建订单失败");
        }
        return Result.success("创建订单成功", result);
    }
}
