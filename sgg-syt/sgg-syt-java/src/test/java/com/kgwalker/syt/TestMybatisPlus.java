package com.kgwalker.syt;

import com.kgwalker.syt.entity.User;
import com.kgwalker.syt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class TestMybatisPlus {
    @Autowired
    private UserService userService;

    // 获取所有记录
    @Test
    void testGetAll() {
        List<User> userList = this.userService.list();
        userList.forEach(user -> log.info(user.toString()));
        Date date = new Date();
        System.out.println(date);
    }

    // 新增
    @Test
    void testInsert() {
        User user = new User().setAge(23).setName("露西").setEmail("3423@gmail.com");
        boolean status = this.userService.save(user);
        log.info("插入操作状态：{}", status);
    }

    @Test
    void testUpdate() {
        User user = new User().setId(111L).setName("麦瑞");
        boolean status = this.userService.updateById(user);
        log.info("更新操作状态：{}", status);
    }
}
