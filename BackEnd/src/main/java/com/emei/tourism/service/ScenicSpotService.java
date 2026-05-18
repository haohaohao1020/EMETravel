package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.ScenicSpot;

import java.util.List;

public interface ScenicSpotService extends IService<ScenicSpot> {
    
    List<ScenicSpot> getSpotList(String keyword);
    
    ScenicSpot getSpotDetail(Long id);
    
    List<ScenicSpot> getRecommendSpots();
}
