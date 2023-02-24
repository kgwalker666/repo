package com.kgwalker.service.impl;

import com.kgwalker.mapper.ScheduleMapper;
import com.kgwalker.entity.Schedule;
import com.kgwalker.service.ScheduleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (Schedule)表服务实现类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Service("scheduleService")
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

}
