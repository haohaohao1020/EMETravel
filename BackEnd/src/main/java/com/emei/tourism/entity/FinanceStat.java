package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_finance_stat")
public class FinanceStat {

    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDate statDate;

    private Integer statType;

    private Integer totalOrder;

    private BigDecimal totalAmount;

    private Integer totalRefund;

    private BigDecimal refundAmount;

    private BigDecimal ticket1Amount;

    private BigDecimal ticket2Amount;

    private BigDecimal ticket3Amount;

    private Integer ticket1Count;

    private Integer ticket2Count;

    private Integer ticket3Count;

    private Integer visitorCount;

    private Integer newUserCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
