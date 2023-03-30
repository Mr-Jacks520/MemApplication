package pers.hence.memapplication.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 21:38
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemContentRequest extends BaseRequest implements Serializable {

    /**
     * 复习内容完成ID
     */
    private Integer completeId;

    /**
     * 搜索关键词
     */
    private String keywords;
}
