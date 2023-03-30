package pers.hence.memapplication.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 21:29
 * @description 帖子发布请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlogPublishRequest extends BaseRequest implements Serializable {

    private String content;
}
