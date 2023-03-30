package pers.hence.memapplication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.hence.memapplication.dao.UserFeedBackDao;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.entity.UserFeedBack;
import pers.hence.memapplication.service.UserFeedBackService;
import pers.hence.memapplication.util.UserUtils;

import javax.servlet.http.HttpServletRequest;

import static pers.hence.memapplication.constant.StatusCode.*;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:36
 * @description 用户反馈业务实现
 */
@Service
public class UserFeedBackServiceImpl extends ServiceImpl<UserFeedBackDao, UserFeedBack> implements UserFeedBackService {

    /**
     * 保存反馈意见
     * @param userId 反馈用户ID
     * @param opinion 反馈意见
     * @param request request
     */
    @Override
    public void saveFeedback(Integer userId, String opinion, HttpServletRequest request) {
        // 1. 校验
        boolean isLogin = UserUtils.isLogin(userId, request);
        if (!isLogin) {
            throw new BusinessException(NOT_AUTH, "未登录");
        }
        boolean isAuth = UserUtils.isAuth(userId, request);
        if (!isAuth) {
            throw new BusinessException(REFUSE, "拒绝访问");
        }
        if ("".equals(opinion)) {
            throw new BusinessException(ERROR, "空");
        }
        // 2. 构造
        UserFeedBack userFeedBack = UserFeedBack.builder().content(opinion).userId(userId).build();
        // 3. 保存
        this.save(userFeedBack);
    }

}
