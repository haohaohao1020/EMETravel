package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.OperateLog;
import com.emei.tourism.mapper.OperateLogMapper;
import com.emei.tourism.service.OperateLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    @Override
    public Page<OperateLog> getLogList(Integer page, Integer size, String module, Integer type, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        Page<OperateLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OperateLog> wrapper = buildQueryWrapper(module, type, status, startTime, endTime);
        wrapper.orderByDesc(OperateLog::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public List<OperateLog> getLogListForExport(String module, Integer type, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OperateLog> wrapper = buildQueryWrapper(module, type, status, startTime, endTime);
        wrapper.orderByDesc(OperateLog::getCreateTime);
        return list(wrapper);
    }

    @Override
    public void saveLog(String module, String operation, Integer type, String method, String params, String result, Integer status, String errorMsg, Long operatorId, String operatorName, String ip, Long costTime) {
        OperateLog log = new OperateLog();
        log.setModule(module);
        log.setOperation(operation);
        log.setType(type);
        log.setMethod(method);
        log.setParams(params);
        log.setResult(result);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setIp(ip);
        log.setCostTime(costTime);
        save(log);
    }

    private LambdaQueryWrapper<OperateLog> buildQueryWrapper(String module, Integer type, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OperateLog> wrapper = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) {
            wrapper.like(OperateLog::getModule, module);
        }
        if (type != null) {
            wrapper.eq(OperateLog::getType, type);
        }
        if (status != null) {
            wrapper.eq(OperateLog::getStatus, status);
        }
        if (startTime != null) {
            wrapper.ge(OperateLog::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(OperateLog::getCreateTime, endTime);
        }
        return wrapper;
    }
}
