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
 * @date 2023/3/30 13:27
 * @description 用户反馈
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_feedback")
public class UserFeedBack implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

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
