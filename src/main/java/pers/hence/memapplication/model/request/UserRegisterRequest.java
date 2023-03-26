package pers.hence.memapplication.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 22:37
 * @description 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userMail;

    private String userPassword;

    private String verifyCode;
}
