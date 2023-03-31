package pers.hence.memapplication.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.request.UserLoginRequest;
import pers.hence.memapplication.model.request.UserRegisterRequest;
import pers.hence.memapplication.model.request.UserSendMailRequest;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.model.vo.UserVO;
import pers.hence.memapplication.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static pers.hence.memapplication.constant.StatusCode.ERROR;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 22:34
 * @description 用户控制层
 */
@Api(tags = "用户模块")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 发送邮箱验证码
     * @param userSendMailRequest request
     * @return null
     */
    @PostMapping("/sendCode")
    public Result<?> sendVerifyCode(@RequestBody UserSendMailRequest userSendMailRequest) {
        // 校验
        if (null == userSendMailRequest) {
            return null;
        }
        if (StringUtils.isBlank(userSendMailRequest.getMailTo())) {
            return null;
        }
        userService.userSendCode(userSendMailRequest.getMailTo());
        return Result.create();
    }

    /**
     * 用户注册
     * @param userRegisterRequest request
     * @return Integer
     */
    @PostMapping("/register")
    public Result<?> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (null == userRegisterRequest) {
            throw new BusinessException(ERROR, "参数为空");
        }

        String userMail = userRegisterRequest.getUserMail();
        String userPass = userRegisterRequest.getUserPassword();
        String verifyCode = userRegisterRequest.getVerifyCode();
        if (StringUtils.isAnyBlank(userMail, userPass, verifyCode)) {
            return null;
        }
        Integer ret = userService.userRegister(userMail, userPass, verifyCode);
        return Result.sucess("注册成功", ret);
    }

    /**
     * 用户登录
     * @param userLoginRequest request
     * @param request request
     * @return userVO
     */
    @PostMapping("/login")
    public Result<?> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (null == userLoginRequest) {
            return Result.fail("参数为空");
        }
        String userMail = userLoginRequest.getUserMail();
        String userPass = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userMail, userPass)) {
            return Result.fail("参数为空");
        }
        UserVO userVO = userService.userLogin(userMail, userPass, request);
        return Result.sucess("", userVO);
    }

    /**
     * 更新用户信息
     * @param userVO 请求体
     * @param request request
     * @return 统一结果封装
     */
    @PutMapping("/update")
    public Result<?> updateInfo(@RequestBody UserVO userVO, HttpServletRequest request) {
        if (null == userVO) {
            return Result.fail("参数为空");
        }
        if (StringUtils.isAnyBlank(userVO.getUserName(), userVO.getUserAvatar(), userVO.getUserMail(), String.valueOf(userVO.getUserSex()))) {
            return Result.fail("参数为空");
        }
        userService.updateUserInfo(userVO, request);
        return Result.create();
    }

    /**
     * 用户退出登录
     * @param request request
     * @return 无
     */
    @PostMapping("/logout")
    public Result<?> userLogout(HttpServletRequest request) {
        if (null == request) {
            throw new BusinessException(ERROR, "");
        }
        userService.userLogout(request);
        return Result.create();
    }
}
