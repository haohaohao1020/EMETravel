package com.emei.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emei.tourism.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
