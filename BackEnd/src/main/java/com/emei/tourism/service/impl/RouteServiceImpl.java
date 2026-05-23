package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Route;
import com.emei.tourism.mapper.RouteMapper;
import com.emei.tourism.service.RouteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements RouteService {

    @Override
    public Page<Route> getRouteList(Integer page, Integer size, String name, Integer status) {
        Page<Route> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Route> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Route::getName, name);
        }
        if (status != null) {
            wrapper.eq(Route::getStatus, status);
        }
        wrapper.orderByAsc(Route::getSort);
        return page(pageParam, wrapper);
    }

    @Override
    public List<Route> getAllRoutes() {
        LambdaQueryWrapper<Route> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Route::getStatus, 1);
        wrapper.orderByAsc(Route::getSort);
        return list(wrapper);
    }

    @Override
    public Route getRouteDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean createRoute(Route route) {
        return save(route);
    }

    @Override
    public boolean updateRoute(Route route) {
        return updateById(route);
    }

    @Override
    public boolean deleteRoute(Long id) {
        return removeById(id);
    }
}
