package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.FinanceStat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FinanceStatService extends IService<FinanceStat> {
    
    FinanceStat getDailyStat(LocalDate date);
    
    List<FinanceStat> getDailyStats(LocalDate startDate, LocalDate endDate);
    
    List<FinanceStat> getMonthlyStats(LocalDate startDate, LocalDate endDate);
    
    List<FinanceStat> getYearlyStats(Integer startYear, Integer endYear);
    
    Map<String, Object> getDashboardStats();
    
    Map<String, Object> getPassengerTrend(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getTicketSalesRatio();
    
    void generateDailyStat(LocalDate date);
    
    void generateMonthlyStat(Integer year, Integer month);
    
    void generateYearlyStat(Integer year);
    
    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate);
    
    Integer getTotalOrders(LocalDate startDate, LocalDate endDate);
    
    Integer getTotalVisitors(LocalDate startDate, LocalDate endDate);
}
