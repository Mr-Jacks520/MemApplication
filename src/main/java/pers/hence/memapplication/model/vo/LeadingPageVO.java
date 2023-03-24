package pers.hence.memapplication.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 18:00
 * @description 引导页用户视图
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeadingPageVO implements Serializable {

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
}
