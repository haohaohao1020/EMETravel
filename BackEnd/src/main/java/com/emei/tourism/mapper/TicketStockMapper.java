package com.emei.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emei.tourism.entity.TicketStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TicketStockMapper extends BaseMapper<TicketStock> {
    
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
