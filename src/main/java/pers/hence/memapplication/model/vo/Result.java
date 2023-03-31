package pers.hence.memapplication.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

import static pers.hence.memapplication.constant.StatusCode.ERROR;
import static pers.hence.memapplication.constant.StatusCode.OK;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:28
 * @description 统一结果封装
 */
@Data
@ToString
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    private Result(){}

    private Result(Integer code) {
        this.code = code;
        this.msg = "";
        this.data = null;
    }

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回封装
     * @param msg 信息
     * @param data 数据
     * @param <T> 泛型
     * @return 封装结果
     */
    public static<T> Result<T> sucess(String msg, T data) {
        return new Result<>(OK, msg, data);
    }

    /**
     * 创建返回结果
     * @param <T>  泛型
     * @return 封装结果
     */
    public static<T> Result<T> create() {
        return new Result<>(OK);
    }

    /**
     * 创建自定义返回结果
     * @param code 状态码
     * @param msg 信息
     * @param <T> 泛型
     * @return 封装结果
     */
    public static<T> Result<T> create(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * 失败返回封装
     * @param msg 信息
     * @param <T> 泛型
     * @return 封装结果
     */
    public static<T> Result<T> fail(String msg) {
        return new Result<>(ERROR, msg);
    }
}
