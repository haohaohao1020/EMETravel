package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.*;
import com.emei.tourism.mapper.FinanceStatMapper;
import com.emei.tourism.mapper.OrderMapper;
import com.emei.tourism.mapper.TicketMapper;
import com.emei.tourism.mapper.UserMapper;
import com.emei.tourism.service.FinanceStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class FinanceStatServiceImpl extends ServiceImpl<FinanceStatMapper, FinanceStat> implements FinanceStatService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public FinanceStat getDailyStat(LocalDate date) {
        LambdaQueryWrapper<FinanceStat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FinanceStat::getStatDate, date);
        wrapper.eq(FinanceStat::getStatType, 1);
        return getOne(wrapper);
    }

    @Override
    public List<FinanceStat> getDailyStats(LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<FinanceStat> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(FinanceStat::getStatDate, startDate, endDate);
        wrapper.eq(FinanceStat::getStatType, 1);
        wrapper.orderByAsc(FinanceStat::getStatDate);
        return list(wrapper);
    }

    @Override
    public List<FinanceStat> getMonthlyStats(LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<FinanceStat> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(FinanceStat::getStatDate, startDate, endDate);
        wrapper.eq(FinanceStat::getStatType, 2);
        wrapper.orderByAsc(FinanceStat::getStatDate);
        return list(wrapper);
    }

    @Override
    public List<FinanceStat> getYearlyStats(Integer startYear, Integer endYear) {
        LambdaQueryWrapper<FinanceStat> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(FinanceStat::getStatDate, LocalDate.of(startYear, 1, 1), LocalDate.of(endYear, 12, 31));
        wrapper.eq(FinanceStat::getStatType, 3);
        wrapper.orderByAsc(FinanceStat::getStatDate);
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate monthStart = today.withDayOfMonth(1);

        FinanceStat todayStat = getDailyStat(today);
        FinanceStat yesterdayStat = getDailyStat(yesterday);

        if (todayStat == null) {
            todayStat = new FinanceStat();
            todayStat.setTotalAmount(BigDecimal.ZERO);
            todayStat.setTotalOrder(0);
            todayStat.setVisitorCount(0);
        }
        if (yesterdayStat == null) {
            yesterdayStat = new FinanceStat();
            yesterdayStat.setTotalAmount(BigDecimal.ZERO);
            yesterdayStat.setTotalOrder(0);
            yesterdayStat.setVisitorCount(0);
        }

        BigDecimal todayRevenue = todayStat.getTotalAmount();
        BigDecimal yesterdayRevenue = yesterdayStat.getTotalAmount();
        BigDecimal revenueGrowth = BigDecimal.ZERO;
        if (yesterdayRevenue.compareTo(BigDecimal.ZERO) > 0) {
            revenueGrowth = todayRevenue.subtract(yesterdayRevenue)
                    .divide(yesterdayRevenue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        BigDecimal monthRevenue = getTotalRevenue(monthStart, today);
        Integer monthOrders = getTotalOrders(monthStart, today);
        Integer monthVisitors = getTotalVisitors(monthStart, today);

        Long totalUsers = userMapper.selectCount(null);
        Long totalTickets = ticketMapper.selectCount(new LambdaQueryWrapper<Ticket>().eq(Ticket::getStatus, 1));

        result.put("todayRevenue", todayRevenue);
        result.put("revenueGrowth", revenueGrowth);
        result.put("todayOrders", todayStat.getTotalOrder());
        result.put("todayVisitors", todayStat.getVisitorCount());
        result.put("monthRevenue", monthRevenue);
        result.put("monthOrders", monthOrders);
        result.put("monthVisitors", monthVisitors);
        result.put("totalUsers", totalUsers);
        result.put("totalTickets", totalTickets);

        return result;
    }

    @Override
    public Map<String, Object> getPassengerTrend(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        List<FinanceStat> stats = getDailyStats(startDate, endDate);
        
        List<String> dates = new ArrayList<>();
        List<Integer> visitorCounts = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.toString());
            final LocalDate currentDate = date;
            FinanceStat stat = stats.stream()
                    .filter(s -> s.getStatDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);
            if (stat != null) {
                visitorCounts.add(stat.getVisitorCount());
                orderCounts.add(stat.getTotalOrder());
            } else {
                visitorCounts.add(0);
                orderCounts.add(0);
            }
        }

        result.put("dates", dates);
        result.put("visitorCounts", visitorCounts);
        result.put("orderCounts", orderCounts);
        return result;
    }

    @Override
    public Map<String, Object> getTicketSalesRatio() {
        Map<String, Object> result = new HashMap<>();
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.between(Order::getCreateTime, monthStart.atStartOfDay(), today.atTime(23, 59, 59));
        orderWrapper.in(Order::getStatus, 2, 3);
        List<Order> orders = orderMapper.selectList(orderWrapper);

        BigDecimal ticket1Amount = BigDecimal.ZERO;
        BigDecimal ticket2Amount = BigDecimal.ZERO;
        BigDecimal ticket3Amount = BigDecimal.ZERO;

        for (Order order : orders) {
            if (order.getTotalAmount() != null) {
                ticket1Amount = ticket1Amount.add(order.getTotalAmount().multiply(new BigDecimal("0.4")));
                ticket2Amount = ticket2Amount.add(order.getTotalAmount().multiply(new BigDecimal("0.35")));
                ticket3Amount = ticket3Amount.add(order.getTotalAmount().multiply(new BigDecimal("0.25")));
            }
        }

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "大门票");
        item1.put("value", ticket1Amount);
        data.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "索道票");
        item2.put("value", ticket2Amount);
        data.add(item2);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("name", "观光车票");
        item3.put("value", ticket3Amount);
        data.add(item3);

        result.put("data", data);
        return result;
    }

    @Override
    public void generateDailyStat(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.between(Order::getCreateTime, startOfDay, endOfDay);
        List<Order> orders = orderMapper.selectList(orderWrapper);

        FinanceStat stat = new FinanceStat();
        stat.setStatDate(date);
        stat.setStatType(1);
        stat.setTotalOrder(orders.size());

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal refundAmount = BigDecimal.ZERO;
        int totalRefund = 0;
        int visitorCount = 0;

        for (Order order : orders) {
            if (order.getTotalAmount() != null) {
                if (order.getStatus() == 4) {
                    refundAmount = refundAmount.add(order.getTotalAmount());
                    totalRefund++;
                } else if (order.getStatus() == 2 || order.getStatus() == 3) {
                    totalAmount = totalAmount.add(order.getTotalAmount());
                    visitorCount++;
                }
            }
        }

        stat.setTotalAmount(totalAmount);
        stat.setTotalRefund(totalRefund);
        stat.setRefundAmount(refundAmount);
        stat.setVisitorCount(visitorCount);

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.between(User::getCreateTime, startOfDay, endOfDay);
        stat.setNewUserCount(Math.toIntExact(userMapper.selectCount(userWrapper)));

        LambdaQueryWrapper<FinanceStat> existingWrapper = new LambdaQueryWrapper<>();
        existingWrapper.eq(FinanceStat::getStatDate, date);
        existingWrapper.eq(FinanceStat::getStatType, 1);
        FinanceStat existing = getOne(existingWrapper);
        if (existing != null) {
            stat.setId(existing.getId());
            updateById(stat);
        } else {
            save(stat);
        }
    }

    @Override
    public void generateMonthlyStat(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<FinanceStat> dailyStats = getDailyStats(startDate, endDate);

        FinanceStat monthlyStat = new FinanceStat();
        monthlyStat.setStatDate(startDate);
        monthlyStat.setStatType(2);

        aggregateStats(monthlyStat, dailyStats);

        LambdaQueryWrapper<FinanceStat> existingWrapper = new LambdaQueryWrapper<>();
        existingWrapper.eq(FinanceStat::getStatDate, startDate);
        existingWrapper.eq(FinanceStat::getStatType, 2);
        FinanceStat existing = getOne(existingWrapper);
        if (existing != null) {
            monthlyStat.setId(existing.getId());
            updateById(monthlyStat);
        } else {
            save(monthlyStat);
        }
    }

    @Override
    public void generateYearlyStat(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<FinanceStat> monthlyStats = getMonthlyStats(startDate, endDate);

        FinanceStat yearlyStat = new FinanceStat();
        yearlyStat.setStatDate(startDate);
        yearlyStat.setStatType(3);

        aggregateStats(yearlyStat, monthlyStats);

        LambdaQueryWrapper<FinanceStat> existingWrapper = new LambdaQueryWrapper<>();
        existingWrapper.eq(FinanceStat::getStatDate, startDate);
        existingWrapper.eq(FinanceStat::getStatType, 3);
        FinanceStat existing = getOne(existingWrapper);
        if (existing != null) {
            yearlyStat.setId(existing.getId());
            updateById(yearlyStat);
        } else {
            save(yearlyStat);
        }
    }

    private void aggregateStats(FinanceStat target, List<FinanceStat> sources) {
        target.setTotalOrder(0);
        target.setTotalAmount(BigDecimal.ZERO);
        target.setTotalRefund(0);
        target.setRefundAmount(BigDecimal.ZERO);
        target.setTicket1Amount(BigDecimal.ZERO);
        target.setTicket2Amount(BigDecimal.ZERO);
        target.setTicket3Amount(BigDecimal.ZERO);
        target.setTicket1Count(0);
        target.setTicket2Count(0);
        target.setTicket3Count(0);
        target.setVisitorCount(0);
        target.setNewUserCount(0);

        for (FinanceStat source : sources) {
            target.setTotalOrder(target.getTotalOrder() + source.getTotalOrder());
            target.setTotalAmount(target.getTotalAmount().add(source.getTotalAmount() != null ? source.getTotalAmount() : BigDecimal.ZERO));
            target.setTotalRefund(target.getTotalRefund() + source.getTotalRefund());
            target.setRefundAmount(target.getRefundAmount().add(source.getRefundAmount() != null ? source.getRefundAmount() : BigDecimal.ZERO));
            target.setTicket1Amount(target.getTicket1Amount().add(source.getTicket1Amount() != null ? source.getTicket1Amount() : BigDecimal.ZERO));
            target.setTicket2Amount(target.getTicket2Amount().add(source.getTicket2Amount() != null ? source.getTicket2Amount() : BigDecimal.ZERO));
            target.setTicket3Amount(target.getTicket3Amount().add(source.getTicket3Amount() != null ? source.getTicket3Amount() : BigDecimal.ZERO));
            target.setTicket1Count(target.getTicket1Count() + source.getTicket1Count());
            target.setTicket2Count(target.getTicket2Count() + source.getTicket2Count());
            target.setTicket3Count(target.getTicket3Count() + source.getTicket3Count());
            target.setVisitorCount(target.getVisitorCount() + source.getVisitorCount());
            target.setNewUserCount(target.getNewUserCount() + source.getNewUserCount());
        }
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        List<FinanceStat> stats = getDailyStats(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;
        for (FinanceStat stat : stats) {
            if (stat.getTotalAmount() != null) {
                total = total.add(stat.getTotalAmount());
            }
        }
        return total;
    }

    @Override
    public Integer getTotalOrders(LocalDate startDate, LocalDate endDate) {
        List<FinanceStat> stats = getDailyStats(startDate, endDate);
        int total = 0;
        for (FinanceStat stat : stats) {
            total += stat.getTotalOrder();
        }
        return total;
    }

    @Override
    public Integer getTotalVisitors(LocalDate startDate, LocalDate endDate) {
        List<FinanceStat> stats = getDailyStats(startDate, endDate);
        int total = 0;
        for (FinanceStat stat : stats) {
            total += stat.getVisitorCount();
        }
        return total;
    }
}
