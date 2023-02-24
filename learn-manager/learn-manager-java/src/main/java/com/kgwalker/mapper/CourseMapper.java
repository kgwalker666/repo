package com.kgwalker.mapper;

import com.kgwalker.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Course)表数据库访问层
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}
