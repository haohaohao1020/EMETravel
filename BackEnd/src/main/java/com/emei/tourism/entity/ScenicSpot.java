package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_scenic_spot")
public class ScenicSpot {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String subtitle;

    private String description;

    private String image;

    private String images;

    private String altitude;

    private String playTime;

    private String openTime;

    private String ticketInfo;

    private String tags;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String tips;

    private Integer isCableEnd;

    private Integer isCableStart;

    private Integer isBusEnd;

    private Integer sort;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
