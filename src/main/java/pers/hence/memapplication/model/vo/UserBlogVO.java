package pers.hence.memapplication.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:41
 * @description 帖子脱敏
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBlogVO implements Serializable {

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
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 发布时间:yyyy-MM-dd
     */
    private String createdTime;
}
