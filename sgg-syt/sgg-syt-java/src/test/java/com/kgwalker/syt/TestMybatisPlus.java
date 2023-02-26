package com.kgwalker.syt;

import com.kgwalker.syt.entity.User;
import com.kgwalker.syt.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMybatisPlus {
    @Autowired
    private UserService userService;

    // 获取所有记录
    @Test
    void testGetAll() {
        List<User> userList = this.userService.list();
        userList.
    }
}
