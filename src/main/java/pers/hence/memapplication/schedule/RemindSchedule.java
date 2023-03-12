package pers.hence.memapplication.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.hence.memapplication.service.MemContentService;

/**
 * @Author https://github.com/Mr-Jacks520
 * @Date 2023/3/12 12:33
 * @Description 定时提醒组件
 */

@Component
public class RemindSchedule {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(RemindSchedule.class);

    /**
     * Bean管理上下文
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 服务层
     */
    @Autowired
    private MemContentService memContentService;

    /**
     * 测试定时提醒: 5 minutes
     */
    @Scheduled(fixedRate = 60 * 5)
    public void remind() {

    }
}
