package com.kgwalker.service.impl;

import com.kgwalker.mapper.CourseMapper;
import com.kgwalker.entity.Course;
import com.kgwalker.service.CourseService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (Course)表服务实现类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

}
