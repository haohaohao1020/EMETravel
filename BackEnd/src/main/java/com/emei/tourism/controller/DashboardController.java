package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.service.FinanceStatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "首页接口")
public class DashboardController {

    @Autowired
    private FinanceStatService financeStatService;

    @GetMapping("/stats")
    @Operation(summary = "获取首页统计数据")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> result = financeStatService.getDashboardStats();
        return Result.success(result);
    }

    @GetMapping("/passengerTrend")
    @Operation(summary = "获取客流趋势")
    public Result<Map<String, Object>> getPassengerTrend(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(6);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        Map<String, Object> result = financeStatService.getPassengerTrend(startDate, endDate);
        return Result.success(result);
    }

    @GetMapping("/ticketSalesRatio")
    @Operation(summary = "获取票务销售占比")
    public Result<Map<String, Object>> getTicketSalesRatio() {
        Map<String, Object> result = financeStatService.getTicketSalesRatio();
        return Result.success(result);
    }

    @GetMapping("/scenicHeatMap")
    @Operation(summary = "获取景点热力图")
    public Result<List<Map<String, Object>>> getScenicHeatMap() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Object> spot1 = new HashMap<>();
        spot1.put("name", "金顶");
        spot1.put("value", 2850);
        spot1.put("lng", 103.3333);
        spot1.put("lat", 29.5333);
        result.add(spot1);

        Map<String, Object> spot2 = new HashMap<>();
        spot2.put("name", "接引殿");
        spot2.put("value", 2100);
        spot2.put("lng", 103.3222);
        spot2.put("lat", 29.5222);
        result.add(spot2);

        Map<String, Object> spot3 = new HashMap<>();
        spot3.put("name", "万年寺");
        spot3.put("value", 1850);
        spot3.put("lng", 103.3111);
        spot3.put("lat", 29.5111);
        result.add(spot3);

        Map<String, Object> spot4 = new HashMap<>();
        spot4.put("name", "清音阁");
        spot4.put("value", 2300);
        spot4.put("lng", 103.3000);
        spot4.put("lat", 29.5000);
        result.add(spot4);

        Map<String, Object> spot5 = new HashMap<>();
        spot5.put("name", "报国寺");
        spot5.put("value", 2600);
        spot5.put("lng", 103.2888);
        spot5.put("lat", 29.4888);
        result.add(spot5);

        return Result.success(result);
    }

    @GetMapping("/cableCarStock")
    @Operation(summary = "获取索道库存状态")
    public Result<List<Map<String, Object>>> getCableCarStock() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Object> stock1 = new HashMap<>();
        stock1.put("timeSlot", "07:00-09:00");
        stock1.put("total", 200);
        stock1.put("sold", 165);
        stock1.put("remaining", 35);
        result.add(stock1);

        Map<String, Object> stock2 = new HashMap<>();
        stock2.put("timeSlot", "09:00-11:00");
        stock2.put("total", 200);
        stock2.put("sold", 189);
        stock2.put("remaining", 11);
        result.add(stock2);

        Map<String, Object> stock3 = new HashMap<>();
        stock3.put("timeSlot", "11:00-13:00");
        stock3.put("total", 200);
        stock3.put("sold", 142);
        stock3.put("remaining", 58);
        result.add(stock3);

        Map<String, Object> stock4 = new HashMap<>();
        stock4.put("timeSlot", "13:00-15:00");
        stock4.put("total", 200);
        stock4.put("sold", 98);
        stock4.put("remaining", 102);
        result.add(stock4);

        Map<String, Object> stock5 = new HashMap<>();
        stock5.put("timeSlot", "15:00-17:00");
        stock5.put("total", 200);
        stock5.put("sold", 56);
        stock5.put("remaining", 144);
        result.add(stock5);

        return Result.success(result);
    }

    @GetMapping("/busStatus")
    @Operation(summary = "获取观光车状态")
    public Result<List<Map<String, Object>>> getBusStatus() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Object> bus1 = new HashMap<>();
        bus1.put("line", "游客中心-接引殿");
        bus1.put("status", "运行中");
        bus1.put("waiting", 35);
        bus1.put("interval", 15);
        result.add(bus1);

        Map<String, Object> bus2 = new HashMap<>();
        bus2.put("line", "接引殿-游客中心");
        bus2.put("status", "运行中");
        bus2.put("waiting", 28);
        bus2.put("interval", 15);
        result.add(bus2);

        Map<String, Object> bus3 = new HashMap<>();
        bus3.put("line", "万年寺-清音阁");
        bus3.put("status", "运行中");
        bus3.put("waiting", 12);
        bus3.put("interval", 20);
        result.add(bus3);

        return Result.success(result);
    }

    @GetMapping("/weatherAlert")
    @Operation(summary = "获取天气预警")
    public Result<Map<String, Object>> getWeatherAlert() {
        Map<String, Object> result = new HashMap<>();
        result.put("temperature", 18);
        result.put("weather", "多云");
        result.put("wind", "东北风 3级");
        result.put("humidity", 65);
        result.put("visibility", "良好");
        result.put("uvIndex", "中等");
        result.put("alertLevel", 0);
        result.put("alertMsg", "");
        result.put("tips", "山顶温度较低，建议携带保暖衣物");
        return Result.success(result);
    }
}
