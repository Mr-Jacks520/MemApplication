package pers.hence.memapplication.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.hence.memapplication.model.entity.User;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/22 23:10
 * @description 用户持久层
 */
@Repository
@Mapper
public interface UserDao extends BaseMapper<User> {
}
