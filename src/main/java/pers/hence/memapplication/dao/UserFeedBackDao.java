package pers.hence.memapplication.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.hence.memapplication.model.entity.UserFeedBack;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:31
 * @description 用户反馈持久层
 */
@Repository
@Mapper
public interface UserFeedBackDao extends BaseMapper<UserFeedBack> {
}
