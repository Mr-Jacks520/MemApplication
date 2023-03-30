package pers.hence.memapplication.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:51
 * @description 历史文件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryFileVO implements Serializable {

    private Integer id;

    private Integer type;

    private String size;

    private String storagePath;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}
