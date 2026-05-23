package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Ticket;
import com.emei.tourism.entity.TicketStock;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TicketService extends IService<Ticket> {
    
    List<Ticket> getTicketList(Integer type);
    
    Ticket getTicketDetail(Long id);
    
    Map<String, Object> getStockInfo(Long ticketId, LocalDate date);
    
    List<String> getTimeSlots(Long ticketId, LocalDate date);
    
    boolean decreaseStock(Long ticketStockId, Integer quantity);
    
    TicketStock getTicketStock(Long ticketId, LocalDate date, String timeSlot);
    
    Page<Ticket> getAdminTicketList(String name, Integer type, Integer status, Integer page, Integer size);
    
    boolean createTicket(Ticket ticket);
    
    boolean updateTicket(Ticket ticket);
    
    boolean deleteTicket(Long id);
    
    boolean toggleTicketStatus(Long id, Integer status);
    
    Page<TicketStock> getTicketStockList(Long ticketId, LocalDate startDate, LocalDate endDate, Integer page, Integer size);
    
    boolean updateTicketStock(TicketStock stock);
    
    boolean lockTicketStock(Long id, Integer lockedStock);
}
