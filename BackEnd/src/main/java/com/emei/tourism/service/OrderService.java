package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Order;
import com.emei.tourism.entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    
    Map<String, Object> createOrder(Map<String, Object> orderData, Long userId);
    
    Page<Order> getOrderList(Long userId, Integer status, Integer page, Integer size);
    
    Map<String, Object> getOrderDetail(Long id, Long userId);
    
    boolean cancelOrder(Long id, Long userId);
    
    boolean refundOrder(Long id, String reason, Long userId);
    
    Map<String, Object> verifyTicket(String verifyCode, Long operatorId);
    
    List<OrderItem> getOrderItems(Long orderId);
}
