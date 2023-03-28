package pers.hence.memapplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.request.UserLoginRequest;
import pers.hence.memapplication.model.request.UserRegisterRequest;
import pers.hence.memapplication.model.request.UserSendMailRequest;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.model.vo.UserVO;
import pers.hence.memapplication.service.UserService;
import pers.hence.memapplication.util.OSSUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static pers.hence.memapplication.constant.StatusCode.*;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 22:34
 * @description 用户控制层
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    private OSSUtil ossUtil;

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

    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) {
        if (file == null) {
            return Result.fail("文件为空");
        }

        return Result.fail("eee");
    }

}
