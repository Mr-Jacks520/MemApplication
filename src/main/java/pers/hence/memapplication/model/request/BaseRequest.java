package pers.hence.memapplication.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 21:25
 * @description 基本请求体
 */
@Data
public class BaseRequest implements Serializable {

    private Integer userId;
}
