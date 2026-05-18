package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    
    Page<Notice> getNoticeList(Integer type, Integer page, Integer size);
    
    Notice getNoticeDetail(Long id);
    
    List<Notice> getTopNotices();
}
