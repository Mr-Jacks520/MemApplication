package pers.hence.memapplication.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 14:51
 * @description 反馈请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FeedBackRequest extends BaseRequest implements Serializable {

    private String content;
}
