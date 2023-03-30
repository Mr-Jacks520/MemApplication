package pers.hence.memapplication.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 22:18
 * @description 用户脱敏
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String userMail;


    /**
     * 管理员标识
     */
    private Integer isAdmin;
}
