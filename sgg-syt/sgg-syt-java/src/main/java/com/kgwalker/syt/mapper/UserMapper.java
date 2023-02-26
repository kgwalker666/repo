package com.kgwalker.syt.mapper;

import com.kgwalker.syt.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author kgwalker
 * @since 2023-02-26 10:43:46
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
