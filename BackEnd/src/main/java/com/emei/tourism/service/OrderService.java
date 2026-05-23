package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Order;
import com.emei.tourism.entity.OrderItem;
import com.emei.tourism.entity.VerifyRecord;

import java.time.LocalDateTime;
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
    
    Page<Order> getAdminOrderList(String orderNo, Integer status, String visitorName, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size);
    
    Map<String, Object> getAdminOrderDetail(Long id);
    
    Page<Order> getRefundList(String orderNo, Integer status, Integer page, Integer size);
    
    boolean approveRefund(Long id, String remark, Long operatorId);
    
    boolean rejectRefund(Long id, String remark, Long operatorId);
    
    List<Order> getOrderListForExport(String orderNo, Integer status, String visitorName, LocalDateTime startTime, LocalDateTime endTime);
    
    Page<VerifyRecord> getVerifyRecordList(String verifyCode, Long ticketId, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size);
}
