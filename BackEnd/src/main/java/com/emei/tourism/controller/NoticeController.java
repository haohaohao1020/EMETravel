package com.emei.tourism.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emei.tourism.common.Result;
import com.emei.tourism.entity.Notice;
import com.emei.tourism.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@Tag(name = "公告/资讯接口")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    @Operation(summary = "获取公告列表")
    public Result<Page<Notice>> getNoticeList(
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Notice> noticePage = noticeService.getNoticeList(type, page, size);
        return Result.success(noticePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取公告详情")
    public Result<Notice> getNoticeDetail(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeDetail(id);
        if (notice == null) {
            return Result.error("公告不存在");
        }
        return Result.success(notice);
    }

    @GetMapping("/top")
    @Operation(summary = "获取置顶公告")
    public Result<List<Notice>> getTopNotices() {
        List<Notice> list = noticeService.getTopNotices();
        return Result.success(list);
    }
}
