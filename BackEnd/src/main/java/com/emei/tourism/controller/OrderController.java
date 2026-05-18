package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Order;
import com.emei.tourism.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Tag(name = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

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

    @GetMapping("/list")
    @Operation(summary = "获取订单列表")
    public Result<Page<Order>> getOrderList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Page<Order> orderPage = orderService.getOrderList(userId, status, page, size);
        return Result.success(orderPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情")
    public Result<Map<String, Object>> getOrderDetail(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> detail = orderService.getOrderDetail(id, userId);
        if (detail == null) {
            return Result.error("订单不存在");
        }
        return Result.success(detail);
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean success = orderService.cancelOrder(id, userId);
        if (!success) {
            return Result.error("取消订单失败");
        }
        return Result.success("取消订单成功", null);
    }

    @PostMapping("/refund/{id}")
    @Operation(summary = "申请退票")
    public Result<Void> refundOrder(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> params,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String reason = params != null ? params.get("reason") : "行程变更";
        boolean success = orderService.refundOrder(id, reason, userId);
        if (!success) {
            return Result.error("退票申请失败");
        }
        return Result.success("退票申请成功", null);
    }

    @PostMapping("/verify")
    @Operation(summary = "核销票券")
    public Result<Map<String, Object>> verifyTicket(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("userId");
        String verifyCode = params.get("verifyCode");
        Map<String, Object> result = orderService.verifyTicket(verifyCode, operatorId);
        if (result == null) {
            return Result.error("核销失败，核销码无效");
        }
        return Result.success("核销成功", result);
    }
}
