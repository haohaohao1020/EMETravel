package com.emei.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_operate_log")
public class OperateLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String module;

    private String operation;

    private Integer type;

    private String method;

    private String params;

    private String result;

    private Integer status;

    private String errorMsg;

    private Long operatorId;

    private String operatorName;

    private String ip;

    private String location;

    private Long costTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
