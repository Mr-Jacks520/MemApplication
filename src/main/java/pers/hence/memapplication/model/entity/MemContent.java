package pers.hence.memapplication.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/12 12:57
 * @Description 记忆内容实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("mem_content")
public class MemContent implements Serializable {

    @TableId(type = IdType.AUTO)
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
     * 是否需要进行提醒复习
     */
    private Integer isComplete;

    /**
     * 存储路径
     */
    private String storagePath;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createdTime;

    /**
     * 用户ID
     */
    private Integer userId;
}
