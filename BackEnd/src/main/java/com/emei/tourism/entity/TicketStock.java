package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_ticket_stock")
public class TicketStock {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ticketId;

    private LocalDate useDate;

    private String timeSlot;

    private Integer totalStock;

    private Integer usedStock;

    private Integer lockedStock;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
