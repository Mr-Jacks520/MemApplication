package pers.hence.memapplication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.hence.memapplication.dao.LeadingPageDao;
import pers.hence.memapplication.model.entity.LeadingPage;
import pers.hence.memapplication.model.vo.LeadingPageVO;
import pers.hence.memapplication.service.LeadingPageService;
import pers.hence.memapplication.util.BeanCopyUtils;

import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:53
 * @description 引导页业务层
 */
@Service
public class LeadingPageServiceImpl extends ServiceImpl<LeadingPageDao, LeadingPage> implements LeadingPageService {

    @Autowired
    private LeadingPageDao leadingPageDao;

    /**
     * 获取引导页信息
     * @return 引导页信息列表
     */
    @Override
    public List<LeadingPageVO> getLeadingPageList() {
        List<LeadingPage> leadingPages = leadingPageDao.selectList(new LambdaQueryWrapper<LeadingPage>()
                .select(LeadingPage::getLeadingTitle, LeadingPage::getLeadingContent, LeadingPage::getLeadingImage)
                .eq(LeadingPage::getIsDelete, 0));
        return BeanCopyUtils.copyList(leadingPages, LeadingPageVO.class);
    }
}
