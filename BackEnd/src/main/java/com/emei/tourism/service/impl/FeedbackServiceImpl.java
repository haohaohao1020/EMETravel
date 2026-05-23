package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Feedback;
import com.emei.tourism.mapper.FeedbackMapper;
import com.emei.tourism.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Override
    public Page<Feedback> getFeedbackList(Integer page, Integer size, Integer type, Integer status, String keyword) {
        Page<Feedback> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Feedback> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(Feedback::getType, type);
        }
        if (status != null) {
            wrapper.eq(Feedback::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Feedback::getTitle, keyword).or().like(Feedback::getContent, keyword);
        }
        wrapper.orderByDesc(Feedback::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public Feedback getFeedbackDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean replyFeedback(Long id, String reply, Long adminId, String adminName) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setReply(reply);
        feedback.setReplyTime(LocalDateTime.now());
        feedback.setReplyAdminId(adminId);
        feedback.setReplyAdminName(adminName);
        feedback.setStatus(2);
        return updateById(feedback);
    }

    @Override
    public boolean updateFeedbackStatus(Long id, Integer status) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setStatus(status);
        return updateById(feedback);
    }
}
