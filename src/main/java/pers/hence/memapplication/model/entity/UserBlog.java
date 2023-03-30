package pers.hence.memapplication.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:23
 * @description 用户帖子类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_blog")
public class UserBlog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 帖子内容:富文本
     */
    private String content;

    /**
     * 点赞数
     */
    private String thumbs;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createdTime;

    /**
     * 发布用户ID
     */
    private Integer userId;
}
