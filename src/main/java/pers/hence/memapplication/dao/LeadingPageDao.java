package pers.hence.memapplication.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.hence.memapplication.model.entity.LeadingPage;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:11
 * @description 引导页持久层
 */
@Repository
@Mapper
public interface LeadingPageDao extends BaseMapper<LeadingPage> {
}
