package pers.hence.memapplication.schedule.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pers.hence.memapplication.constant.GlobalConstant;
import pers.hence.memapplication.schedule.events.RemindEvent;
import pers.hence.memapplication.server.WebSocketServer;

import java.util.Set;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 14:24
 * @description 提醒事件监听
 */
@Component
@Slf4j
public class RemindEventListener implements ApplicationListener<RemindEvent>, BaseEventListener {

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void onApplicationEvent(RemindEvent event) {
        Set<String> userIds = event.getUserIds();
        // WebSocket发布消息
        webSocketServer.sendInfo(userIds, GlobalConstant.REMIND_MESSAGE);
        // TODO: 利用策略模式选择发布消息的方式: 1. 个性化提醒; 2. 普通提醒
        log.info("提醒完成");
    }
}
