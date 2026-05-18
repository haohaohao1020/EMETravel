package com.emei.tourism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emei.tourism.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    
    Map<String, Object> login(String phone, String password);
    
    User getUserInfo(Long userId);
    
    boolean updateUserInfo(User user);
}
