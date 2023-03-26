package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:50
 * @description
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userMail 用户邮箱
     * @param userPassword 用户密码
     * @param mailCode 验证码
     * @return 用户ID
     */
    Integer userRegister(String userMail, String userPassword, String mailCode);

    /**
     * 用户登录
     * @param userMail 用户邮箱
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏信息
     */
    UserVO userLogin(String userMail, String userPassword, HttpServletRequest request);

    /**
     * 发送验证码
     * @param mailTo 发送对象
     */
    void userSendCode(String mailTo);
}
