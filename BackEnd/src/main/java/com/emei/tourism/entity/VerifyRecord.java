package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_verify_record")
public class VerifyRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long orderItemId;

    private String verifyCode;

    private Long ticketId;

    private Long operatorId;

    private String operatorName;

    private Integer verifyType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
