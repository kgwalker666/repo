package com.kgwalker.service.impl;

import com.kgwalker.mapper.GroupMapper;
import com.kgwalker.entity.Group;
import com.kgwalker.service.GroupService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (Group)表服务实现类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Service("groupService")
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
