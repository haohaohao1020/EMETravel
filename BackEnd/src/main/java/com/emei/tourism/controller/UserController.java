package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.entity.User;
import com.emei.tourism.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public Result<Void> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        boolean success = userService.updateUserInfo(user);
        if (!success) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功", null);
    }
}
