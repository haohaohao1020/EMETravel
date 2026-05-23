package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Feedback;
import com.emei.tourism.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "反馈接口")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/list")
    @Operation(summary = "获取反馈列表")
    public Result<Page<Feedback>> getFeedbackList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        Page<Feedback> result = feedbackService.getFeedbackList(page, size, type, status, keyword);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取反馈详情")
    public Result<Feedback> getFeedbackDetail(@PathVariable Long id) {
        Feedback result = feedbackService.getFeedbackDetail(id);
        return Result.success(result);
    }

    @PutMapping("/{id}/reply")
    @Operation(summary = "回复反馈")
    public Result<Void> replyFeedback(@PathVariable Long id, @RequestBody Map<String, String> params, HttpServletRequest request) {
        String reply = params.get("reply");
        Long adminId = (Long) request.getAttribute("adminId");
        String adminName = (String) request.getAttribute("adminName");
        boolean success = feedbackService.replyFeedback(id, reply, adminId, adminName);
        if (!success) {
            return Result.error("回复失败");
        }
        return Result.success("回复成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新反馈状态")
    public Result<Void> updateFeedbackStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        boolean success = feedbackService.updateFeedbackStatus(id, status);
        if (!success) {
            return Result.error("状态更新失败");
        }
        return Result.success("状态更新成功", null);
    }
}
