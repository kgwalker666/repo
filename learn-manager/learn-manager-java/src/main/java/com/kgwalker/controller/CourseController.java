package com.kgwalker.controller;

import com.kgwalker.common.R;
import com.kgwalker.service.CourseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (Course)表控制层
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@RestController
@RequestMapping("course")
public class CourseController {
    /**
     * 服务对象
     */
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("list")
    public R getCourseList() {
        return R.status().data(this.courseService.list());
    }
}
