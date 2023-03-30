package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.hence.memapplication.model.entity.UserFeedBack;

import javax.servlet.http.HttpServletRequest;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:34
 * @description 用户反馈业务接口
 */
public interface UserFeedBackService extends IService<UserFeedBack> {

    /**
     * 反馈意见
     * @param userId 反馈用户ID
     * @param opinion 反馈意见
     * @param request request
     */
    void saveFeedback(Integer userId, String opinion, HttpServletRequest request);
}
