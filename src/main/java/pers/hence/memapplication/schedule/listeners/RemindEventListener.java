package pers.hence.memapplication.schedule.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import pers.hence.memapplication.schedule.events.RemindEvent;

import java.util.Map;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 14:24
 * @description 提醒事件监听
 */
@Slf4j
public class RemindEventListener implements ApplicationListener<RemindEvent>, BaseEventListener {

    @Override
    public void onApplicationEvent(RemindEvent event) {
        Map<Integer, String> remindMessage = event.getRemindMessage();
        // TODO: WebSocket发布消息
        log.info("提醒完成");
    }
}
