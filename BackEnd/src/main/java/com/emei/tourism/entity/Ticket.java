package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ticket")
public class Ticket {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer type;

    private BigDecimal price;

    private Integer stock;

    private Integer validDays;

    private String useRule;

    private String description;

    private String image;

    private Integer status;

    private String timeSlots;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
