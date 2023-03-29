package pers.hence.memapplication.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 13:52
 * @description 记忆内容脱敏
 */
@Data
public class MemContentVO implements Serializable {

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 大小
     */
    private String size;

    /**
     * 复习时间列表JSON
     */
    private String reviewTimes;

    /**
     * 下次复习时间
     */
    private String nextReview;

    /**
     * 存储路径
     */
    private String storagePath;

}
