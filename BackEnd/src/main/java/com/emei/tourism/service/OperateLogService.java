package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.OperateLog;

import java.time.LocalDateTime;
import java.util.List;

public interface OperateLogService extends IService<OperateLog> {
    
    Page<OperateLog> getLogList(Integer page, Integer size, String module, Integer type, Integer status, LocalDateTime startTime, LocalDateTime endTime);
    
    List<OperateLog> getLogListForExport(String module, Integer type, Integer status, LocalDateTime startTime, LocalDateTime endTime);
    
    void saveLog(String module, String operation, Integer type, String method, String params, String result, Integer status, String errorMsg, Long operatorId, String operatorName, String ip, Long costTime);
}
