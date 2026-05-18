package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Order;
import com.emei.tourism.entity.OrderItem;
import com.emei.tourism.entity.Ticket;
import com.emei.tourism.entity.TicketStock;
import com.emei.tourism.mapper.OrderItemMapper;
import com.emei.tourism.mapper.OrderMapper;
import com.emei.tourism.service.OrderService;
import com.emei.tourism.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private TicketService ticketService;

    @Override
    @Transactional
    public Map<String, Object> createOrder(Map<String, Object> orderData, Long userId) {
        Long ticketId = Long.valueOf(orderData.get("ticketId").toString());
        Integer quantity = Integer.valueOf(orderData.get("quantity").toString());
        String useDateStr = (String) orderData.get("useDate");
        String timeSlot = (String) orderData.get("timeSlot");
        String visitorName = (String) orderData.get("visitorName");
        String visitorPhone = (String) orderData.get("visitorPhone");
        
        LocalDate useDate = LocalDate.parse(useDateStr);
        
        Ticket ticket = ticketService.getById(ticketId);
        if (ticket == null) {
            return null;
        }
        
        TicketStock stock = ticketService.getTicketStock(ticketId, useDate, timeSlot);
        int available = stock.getTotalStock() - stock.getUsedStock() - stock.getLockedStock();
        if (available < quantity) {
            return null;
        }
        
        String orderNo = "EM" + System.currentTimeMillis();
        String verifyCode = generateVerifyCode();
        BigDecimal totalAmount = ticket.getPrice().multiply(new BigDecimal(quantity));
        
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setVisitorName(visitorName);
        order.setVisitorPhone(visitorPhone);
        order.setTotalAmount(totalAmount);
        order.setStatus(1);
        order.setVerifyCode(verifyCode);
        save(order);
        
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setOrderNo(orderNo);
        item.setTicketId(ticketId);
        item.setTicketName(ticket.getName());
        item.setTicketType(ticket.getType());
        item.setPrice(ticket.getPrice());
        item.setQuantity(quantity);
        item.setUseDate(useDate);
        item.setTimeSlot(timeSlot);
        item.setVerifyCode(verifyCode);
        item.setVerifyStatus(0);
        orderItemMapper.insert(item);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", orderNo);
        result.put("verifyCode", verifyCode);
        result.put("totalAmount", totalAmount);
        return result;
    }

    @Override
    public Page<Order> getOrderList(Long userId, Integer status, Integer page, Integer size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null && status > 0) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public Map<String, Object> getOrderDetail(Long id, Long userId) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            return null;
        }
        
        List<OrderItem> items = getOrderItems(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return result;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long id, Long userId) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(userId) || order.getStatus() != 1) {
            return false;
        }
        order.setStatus(0);
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean refundOrder(Long id, String reason, Long userId) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(userId) || 
            (order.getStatus() != 1 && order.getStatus() != 2)) {
            return false;
        }
        order.setStatus(4);
        order.setRefundReason(reason);
        order.setRefundTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    @Transactional
    public Map<String, Object> verifyTicket(String verifyCode, Long operatorId) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getVerifyCode, verifyCode);
        Order order = getOne(orderWrapper);
        if (order == null) {
            return null;
        }
        
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId())
                   .eq(OrderItem::getVerifyStatus, 0);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
        if (items.isEmpty()) {
            return null;
        }
        
        for (OrderItem item : items) {
            item.setVerifyStatus(1);
            item.setVerifyTime(LocalDateTime.now());
            orderItemMapper.updateById(item);
        }
        
        order.setStatus(3);
        updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        result.put("verifyTime", LocalDateTime.now());
        result.put("ticketCount", items.size());
        return result;
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    private String generateVerifyCode() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }
}
