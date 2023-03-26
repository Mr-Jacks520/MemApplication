package pers.hence.memapplication.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 23:58
 * @description 邮件请求
 */
@Data
public class UserSendMailRequest implements Serializable {
    private String mailTo;
}
