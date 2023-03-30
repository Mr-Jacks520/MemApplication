package pers.hence.memapplication.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.hence.memapplication.model.entity.UserBlog;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:30
 * @description 用户帖子持久层
 */
@Repository
@Mapper
public interface UserBlogDao extends BaseMapper<UserBlog> {
}
