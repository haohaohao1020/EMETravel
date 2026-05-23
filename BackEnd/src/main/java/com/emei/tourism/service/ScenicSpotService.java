package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.ScenicSpot;

import java.util.List;

public interface ScenicSpotService extends IService<ScenicSpot> {
    
    List<ScenicSpot> getSpotList(String keyword);
    
    ScenicSpot getSpotDetail(Long id);
    
    List<ScenicSpot> getRecommendSpots();
    
    Page<ScenicSpot> getAdminSpotList(String name, Integer status, Integer page, Integer size);
    
    boolean createSpot(ScenicSpot spot);
    
    boolean updateSpot(ScenicSpot spot);
    
    boolean deleteSpot(Long id);
    
    boolean toggleSpotStatus(Long id, Integer status);
}
