package pers.hence.memapplication.util;

import pers.hence.memapplication.constant.UserConstant;
import pers.hence.memapplication.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 14:38
 * @description 关于用户的工具类
 */
public class UserUtils {

    // TODO: 考虑AOP横切校验

    /**
     * 判断用户是否登录
     * @param userId 用户ID
     * @param request 请求
     * @return 是否登录
     */
    public static boolean isLogin(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return false;
        }
        Object object = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return null != object;
    }


    /**
     * 请求是否合法
     * @param userId 用户ID
     * @param request 请求
     * @return 是否合法
     */
    public static boolean isAuth(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return false;
        }
        Object object = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (null == object) {
            return false;
        }
        UserVO userVO = (UserVO) object;
        return userId.equals(userVO.getId());
    }
}
