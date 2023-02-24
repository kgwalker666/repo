package com.kgwalker.mapper;

import com.kgwalker.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Group)表数据库访问层
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
