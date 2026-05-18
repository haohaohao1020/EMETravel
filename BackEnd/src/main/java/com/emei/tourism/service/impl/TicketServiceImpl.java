package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Ticket;
import com.emei.tourism.entity.TicketStock;
import com.emei.tourism.mapper.TicketMapper;
import com.emei.tourism.mapper.TicketStockMapper;
import com.emei.tourism.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Autowired
    private TicketStockMapper ticketStockMapper;

    @Override
    public List<Ticket> getTicketList(Integer type) {
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ticket::getStatus, 1);
        if (type != null) {
            wrapper.eq(Ticket::getType, type);
        }
        wrapper.orderByAsc(Ticket::getType).orderByAsc(Ticket::getPrice);
        return list(wrapper);
    }

    @Override
    public Ticket getTicketDetail(Long id) {
        return getById(id);
    }

    @Override
    public Map<String, Object> getStockInfo(Long ticketId, LocalDate date) {
        LambdaQueryWrapper<TicketStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TicketStock::getTicketId, ticketId)
                .eq(TicketStock::getUseDate, date);
        List<TicketStock> stocks = ticketStockMapper.selectList(wrapper);
        
        int totalStock = 0;
        int usedStock = 0;
        int availableStock = 0;
        
        for (TicketStock stock : stocks) {
            totalStock += stock.getTotalStock() != null ? stock.getTotalStock() : 0;
            usedStock += stock.getUsedStock() != null ? stock.getUsedStock() : 0;
            availableStock += (stock.getTotalStock() != null ? stock.getTotalStock() : 0) - 
                             (stock.getUsedStock() != null ? stock.getUsedStock() : 0) - 
                             (stock.getLockedStock() != null ? stock.getLockedStock() : 0);
        }
        
        if (stocks.isEmpty()) {
            Ticket ticket = getById(ticketId);
            if (ticket != null && ticket.getType() == 2) {
                totalStock = 1000;
                availableStock = 1000;
            } else {
                totalStock = 99999;
                availableStock = 99999;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalStock", totalStock);
        result.put("usedStock", usedStock);
        result.put("availableStock", availableStock);
        return result;
    }

    @Override
    public List<String> getTimeSlots(Long ticketId, LocalDate date) {
        Ticket ticket = getById(ticketId);
        if (ticket != null && ticket.getTimeSlots() != null) {
            return Arrays.asList(ticket.getTimeSlots().split(","));
        }
        List<String> slots = new ArrayList<>();
        slots.add("07:00-09:00");
        slots.add("09:00-11:00");
        slots.add("11:00-13:00");
        slots.add("13:00-15:00");
        slots.add("15:00-17:00");
        return slots;
    }

    @Override
    public boolean decreaseStock(Long ticketStockId, Integer quantity) {
        int rows = ticketStockMapper.decreaseStock(ticketStockId, quantity);
        return rows > 0;
    }

    @Override
    public TicketStock getTicketStock(Long ticketId, LocalDate date, String timeSlot) {
        LambdaQueryWrapper<TicketStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TicketStock::getTicketId, ticketId)
                .eq(TicketStock::getUseDate, date)
                .eq(TicketStock::getTimeSlot, timeSlot);
        TicketStock stock = ticketStockMapper.selectOne(wrapper);
        
        if (stock == null) {
            stock = new TicketStock();
            stock.setTicketId(ticketId);
            stock.setUseDate(date);
            stock.setTimeSlot(timeSlot);
            Ticket ticket = getById(ticketId);
            if (ticket != null && ticket.getType() == 2) {
                stock.setTotalStock(1000);
            } else {
                stock.setTotalStock(99999);
            }
            stock.setUsedStock(0);
            stock.setLockedStock(0);
            ticketStockMapper.insert(stock);
        }
        return stock;
    }
}
