package pers.hence.memapplication.schedule;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.hence.memapplication.constant.GlobalConstant;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.vo.MemContentVO;
import pers.hence.memapplication.schedule.events.PreCachedEvent;
import pers.hence.memapplication.schedule.events.RemindEvent;
import pers.hence.memapplication.service.MemContentService;
import pers.hence.memapplication.util.BeanCopyUtils;
import pers.hence.memapplication.util.RedisUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/12 12:33
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
     * Redis工具类
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试定时提醒用户复习: 5 minutes
     */
    @Scheduled(fixedRate = 60 * 5)
    public void remind() {
        // 1. 获取Redis中的内容
        Set<String> userIds = redisUtil.hashKeys(GlobalConstant.PREJOB_PREFIX);
        Map<Integer, String> task = new ConcurrentHashMap<>(userIds.size());
        for (String userId : userIds) {
            task.put(Integer.parseInt(userId), GlobalConstant.REMIND_MESSAGE);
        }
        // 2. 创建事件
        RemindEvent remindEvent = new RemindEvent(task);
        // 3. 发布事件
        applicationContext.publishEvent(remindEvent);
        log.info("提醒事件发布完成");
    }

    /**
     * 测试定时缓存预热
     */
    @Scheduled(fixedRate = 60 * 4)
    public void cacheMemContent() {
        // 获取当前日期
        String curTime = DateUtil.today();
        // 1. 从数据库中查出所有数据
        QueryWrapper<MemContent> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("is_complete", 0)
                .eq("is_delete", 0);
        List<MemContent> memContents = memContentService.list(queryWrapper);
        // 2. 遍历加入map
        Map<Integer, List<MemContentVO>> task = new ConcurrentHashMap<>(memContents.size());
        for (MemContent memContent: memContents) {
            // 3. 比较当前日期与下次复习日期
            String nextReview = memContent.getNextReview();
            try {
                int compare = DateUtil.compare(DateFormat.getDateInstance().parse(curTime), DateFormat.getDateInstance().parse(nextReview));
                if (compare < 0) {
                    // TODO: 重新生成复习时间表并写入数据库
                    log.info("ops...");
                }
            } catch (ParseException e) {
                log.info(e.getMessage());
                continue;
            }
            Integer userId = memContent.getUserId();
            MemContentVO contentVO = BeanCopyUtils.copyObject(memContent, MemContentVO.class);
            List<MemContentVO> voList = task.getOrDefault(userId, new ArrayList<>());
            voList.add(contentVO);
            task.put(userId, voList);
        }
        // 4. 创建缓存事件
        PreCachedEvent preCachedEvent = new PreCachedEvent(task);
        // 5. 发布通知
        applicationContext.publishEvent(preCachedEvent);
        log.info("缓存预热发布完成");
    }
}
