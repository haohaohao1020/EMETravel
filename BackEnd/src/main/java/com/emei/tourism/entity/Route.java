package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_route")
public class Route {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String subtitle;

    private String description;

    private String image;

    private String spotIds;

    private String distance;

    private String duration;

    private Integer difficulty;

    private Integer type;

    private String tips;

    private Integer sort;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
