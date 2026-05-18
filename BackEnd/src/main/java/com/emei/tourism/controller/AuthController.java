package com.emei.tourism.controller;

import com.emei.tourism.common.Result;
import com.emei.tourism.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        
        if (phone == null || phone.length() != 11) {
            return Result.error("请输入正确的手机号");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("请输入密码");
        }
        
        Map<String, Object> result = userService.login(phone, password);
        if (result == null) {
            return Result.error("手机号或密码错误");
        }
        return Result.success("登录成功", result);
    }
}
