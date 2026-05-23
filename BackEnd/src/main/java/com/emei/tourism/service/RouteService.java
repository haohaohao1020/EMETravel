package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Route;

import java.util.List;

public interface RouteService extends IService<Route> {
    
    Page<Route> getRouteList(Integer page, Integer size, String name, Integer status);
    
    List<Route> getAllRoutes();
    
    Route getRouteDetail(Long id);
    
    boolean createRoute(Route route);
    
    boolean updateRoute(Route route);
    
    boolean deleteRoute(Long id);
}
