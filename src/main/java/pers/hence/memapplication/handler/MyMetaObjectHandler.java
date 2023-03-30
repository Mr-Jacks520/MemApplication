package pers.hence.memapplication.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/12 13:35
 * @Description 自定义插入更新时间策略
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final Log log = LogFactory.getLog(MyMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.fillStrategy(metaObject, "createdTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.fillStrategy(metaObject, "updatedTime", LocalDateTime.now());
    }
}
