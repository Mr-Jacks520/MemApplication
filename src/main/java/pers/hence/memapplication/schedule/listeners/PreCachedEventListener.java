package pers.hence.memapplication.schedule.listeners;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pers.hence.memapplication.model.vo.MemContentVO;
import pers.hence.memapplication.schedule.events.PreCachedEvent;
import pers.hence.memapplication.util.RedisUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static pers.hence.memapplication.constant.GlobalConstant.PREJOB_PREFIX;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 13:56
 * @description 预热事件监听类
 */
@Component
@Slf4j
public class PreCachedEventListener implements ApplicationListener<PreCachedEvent>, BaseEventListener {

    @Autowired
    private RedisUtil redisUtil;

    private static final long EXPIRE = 39600;

    /**
     * 进行redis缓存预热
     * @param event 事件
     */
    @Override
    public void onApplicationEvent(PreCachedEvent event) {
        // 获取事件
        Map<Integer, List<MemContentVO>> task = event.getTask();
        Map<String, String> map = new ConcurrentHashMap<>(task.size());
        Set<Map.Entry<Integer, List<MemContentVO>>> entrySet = task.entrySet();
        Gson gson = new Gson();
        for (Map.Entry<Integer, List<MemContentVO>> next : entrySet) {
            List<MemContentVO> voList = next.getValue();
            Integer key = next.getKey();
            if (null == key || voList == null) {
                continue;
            }
            // JSON化
            String value = gson.toJson(voList);
            map.put(String.valueOf(key), value);
        }
        // 加入缓存
        redisUtil.add(PREJOB_PREFIX, map);
        // 设置过期时间: 11h
        redisUtil.expire(PREJOB_PREFIX, EXPIRE);
        log.info("缓存预热完成");
    }
}
