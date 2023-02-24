package com.kgwalker.mapper;

import com.kgwalker.entity.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Schedule)表数据库访问层
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

}
