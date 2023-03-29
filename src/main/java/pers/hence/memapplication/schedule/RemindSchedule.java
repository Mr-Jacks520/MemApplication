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
import pers.hence.memapplication.util.AlgorithmUtils;
import pers.hence.memapplication.util.BeanCopyUtils;
import pers.hence.memapplication.util.RedisUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
     * 定时提醒用户复习:每天22点提醒一次
     */
    @Scheduled(cron = "0 0 22 * * *")
    public void remind() {
        // 1. 获取Redis中的内容
        Set<String> userIds = redisUtil.hashKeys(GlobalConstant.PREJOB_PREFIX);
        // 2. 创建事件
        RemindEvent remindEvent = new RemindEvent(userIds);
        // 3. 发布事件
        applicationContext.publishEvent(remindEvent);
        log.info("提醒事件发布完成");
    }

    /**
     * 定时缓存预热:每天3点执行一次
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cacheMemContent() {
        // 获取当前日期
        String curTime = DateUtil.today();
        // 1. 从数据库中查出所有记忆内容
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
                    // 重新生成复习时间表并刷入数据库
                    String timePoints = AlgorithmUtils.ebenhausCurve(curTime);
                    String nextReviewTime = AlgorithmUtils.getNextReviewTime(curTime);
                    memContent.setReviewTimes(timePoints);
                    memContent.setNextReview(nextReviewTime);
                    // 刷入数据库不进行此次缓存
                    QueryWrapper<MemContent> query = new QueryWrapper<>();
                    query.eq("id", memContent.getId());
                    memContentService.update(query);
                } else if (compare == 0) {
                    // 相等加入缓存预热
                    Integer userId = memContent.getUserId();
                    MemContentVO contentVO = BeanCopyUtils.copyObject(memContent, MemContentVO.class);
                    List<MemContentVO> voList = task.getOrDefault(userId, new ArrayList<>());
                    voList.add(contentVO);
                    task.put(userId, voList);
                }
                // 若大于什么都不管,说明此时未到复习时间
            } catch (ParseException e) {
                log.info(e.getMessage());
            }
        }
        // 4. 创建缓存事件
        PreCachedEvent preCachedEvent = new PreCachedEvent(task);
        // 5. 发布通知
        applicationContext.publishEvent(preCachedEvent);
        log.info("缓存预热发布完成");
    }
}
