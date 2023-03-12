package pers.hence.memapplication.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.hence.memapplication.model.entity.MemContent;

/**
 * @Author https://github.com/Mr-Jacks520
 * @Date 2023/3/12 13:05
 * @Description
 */

@Repository
@Mapper
public interface MemContentDao extends BaseMapper<MemContent> {
}
