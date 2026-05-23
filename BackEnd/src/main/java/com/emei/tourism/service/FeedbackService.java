package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Feedback;

public interface FeedbackService extends IService<Feedback> {
    
    Page<Feedback> getFeedbackList(Integer page, Integer size, Integer type, Integer status, String keyword);
    
    Feedback getFeedbackDetail(Long id);
    
    boolean replyFeedback(Long id, String reply, Long adminId, String adminName);
    
    boolean updateFeedbackStatus(Long id, Integer status);
}
