package pers.hence.memapplication.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:03
 * @description 引导页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("leading_page")
public class LeadingPage implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 引导页标题
     */
    private String leadingTitle;

    /**
     * 引导页正文
     */
    private String leadingContent;

    /**
     * 引导页图片URL
     */
    private String leadingImage;

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
}
