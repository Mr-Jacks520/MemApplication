package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.hence.memapplication.model.entity.LeadingPage;
import pers.hence.memapplication.model.vo.LeadingPageVO;

import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:53
 * @description
 */
public interface LeadingPageService extends IService<LeadingPage> {
    List<LeadingPageVO> getLeadingPageList();
}
