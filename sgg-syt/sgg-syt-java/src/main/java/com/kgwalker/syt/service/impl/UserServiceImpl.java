package com.kgwalker.syt.service.impl;

import com.kgwalker.syt.mapper.UserMapper;
import com.kgwalker.syt.entity.User;
import com.kgwalker.syt.service.UserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户表(User)表服务实现类
 *
 * @author kgwalker
 * @since 2023-02-26 10:43:46
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
