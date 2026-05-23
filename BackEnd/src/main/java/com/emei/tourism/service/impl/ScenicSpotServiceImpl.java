package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.ScenicSpot;
import com.emei.tourism.mapper.ScenicSpotMapper;
import com.emei.tourism.service.ScenicSpotService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicSpotServiceImpl extends ServiceImpl<ScenicSpotMapper, ScenicSpot> implements ScenicSpotService {

    @Override
    public List<ScenicSpot> getSpotList(String keyword) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ScenicSpot::getName, keyword)
                   .or().like(ScenicSpot::getSubtitle, keyword);
        }
        wrapper.orderByAsc(ScenicSpot::getSort);
        return list(wrapper);
    }

    @Override
    public ScenicSpot getSpotDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<ScenicSpot> getRecommendSpots() {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1)
               .orderByAsc(ScenicSpot::getSort)
               .last("LIMIT 6");
        return list(wrapper);
    }

    @Override
    public Page<ScenicSpot> getAdminSpotList(String name, Integer status, Integer page, Integer size) {
        Page<ScenicSpot> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(ScenicSpot::getName, name);
        }
        if (status != null) {
            wrapper.eq(ScenicSpot::getStatus, status);
        }
        wrapper.orderByAsc(ScenicSpot::getSort).orderByDesc(ScenicSpot::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public boolean createSpot(ScenicSpot spot) {
        spot.setStatus(1);
        return save(spot);
    }

    @Override
    public boolean updateSpot(ScenicSpot spot) {
        return updateById(spot);
    }

    @Override
    public boolean deleteSpot(Long id) {
        return removeById(id);
    }

    @Override
    public boolean toggleSpotStatus(Long id, Integer status) {
        ScenicSpot spot = new ScenicSpot();
        spot.setId(id);
        spot.setStatus(status);
        return updateById(spot);
    }
}
