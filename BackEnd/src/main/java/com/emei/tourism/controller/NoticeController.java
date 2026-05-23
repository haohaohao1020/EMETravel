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
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
@Tag(name = "公告/资讯接口")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    @Operation(summary = "获取公告列表")
    public Result<Page<Notice>> getNoticeList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Notice> noticePage = noticeService.getAdminNoticeList(title, type, status, page, size);
        return Result.success(noticePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取公告详情")
    public Result<Notice> getNotice(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeDetail(id);
        if (notice == null) {
            return Result.error("公告不存在");
        }
        return Result.success(notice);
    }

    @PostMapping
    @Operation(summary = "新增公告")
    public Result<Void> createNotice(@RequestBody Notice notice) {
        boolean success = noticeService.createNotice(notice);
        if (!success) {
            return Result.error("新增失败");
        }
        return Result.success("新增成功", null);
    }

    @PutMapping
    @Operation(summary = "更新公告")
    public Result<Void> updateNotice(@RequestBody Notice notice) {
        boolean success = noticeService.updateNotice(notice);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    public Result<Void> deleteNotice(@PathVariable Long id) {
        boolean success = noticeService.deleteNotice(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换公告状态")
    public Result<Void> toggleNoticeStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        boolean success = noticeService.toggleNoticeStatus(id, status);
        if (!success) {
            return Result.error("状态切换失败");
        }
        return Result.success("状态切换成功", null);
    }

    @GetMapping("/top")
    @Operation(summary = "获取置顶公告")
    public Result<List<Notice>> getTopNotices() {
        List<Notice> list = noticeService.getTopNotices();
        return Result.success(list);
    }
}
