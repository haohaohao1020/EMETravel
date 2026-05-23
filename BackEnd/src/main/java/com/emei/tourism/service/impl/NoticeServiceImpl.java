package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.Notice;
import com.emei.tourism.mapper.NoticeMapper;
import com.emei.tourism.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Page<Notice> getNoticeList(Integer type, Integer page, Integer size) {
        Page<Notice> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1);
        if (type != null) {
            wrapper.eq(Notice::getType, type);
        }
        wrapper.orderByDesc(Notice::getIsTop).orderByDesc(Notice::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public Notice getNoticeDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<Notice> getTopNotices() {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1)
               .eq(Notice::getIsTop, 1)
               .orderByDesc(Notice::getSort)
               .last("LIMIT 5");
        return list(wrapper);
    }

    @Override
    public Page<Notice> getAdminNoticeList(String title, Integer type, Integer status, Integer page, Integer size) {
        Page<Notice> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like(Notice::getTitle, title);
        }
        if (type != null) {
            wrapper.eq(Notice::getType, type);
        }
        if (status != null) {
            wrapper.eq(Notice::getStatus, status);
        }
        wrapper.orderByDesc(Notice::getIsTop).orderByDesc(Notice::getCreateTime);
        return page(pageParam, wrapper);
    }

    @Override
    public boolean createNotice(Notice notice) {
        notice.setStatus(1);
        return save(notice);
    }

    @Override
    public boolean updateNotice(Notice notice) {
        return updateById(notice);
    }

    @Override
    public boolean deleteNotice(Long id) {
        return removeById(id);
    }

    @Override
    public boolean toggleNoticeStatus(Long id, Integer status) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(status);
        return updateById(notice);
    }
}
