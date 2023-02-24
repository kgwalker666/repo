package com.kgwalker.controller;

import com.kgwalker.service.GroupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (Group)表控制层
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@RestController
@RequestMapping("group")
public class GroupController {
    /**
     * 服务对象
     */
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
}
