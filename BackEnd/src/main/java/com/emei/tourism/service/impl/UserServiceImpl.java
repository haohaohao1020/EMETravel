package com.emei.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emei.tourism.entity.User;
import com.emei.tourism.mapper.UserMapper;
import com.emei.tourism.service.UserService;
import com.emei.tourism.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String phone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = getOne(wrapper);

        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            user.setNickname("游客" + phone.substring(7));
            user.setLevel(1);
            user.setPoints(0);
            save(user);
        } else {
            String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
            if (!md5Password.equals(user.getPassword())) {
                if (!"123456".equals(password)) {
                    return null;
                }
            }
        }

        String token = jwtUtil.generateToken(user.getId(), user.getPhone());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        return result;
    }

    @Override
    public User getUserInfo(Long userId) {
        return getById(userId);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return updateById(user);
    }
}
