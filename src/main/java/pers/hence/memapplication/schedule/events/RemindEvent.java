package pers.hence.memapplication.schedule.events;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 14:22
 * @description 提醒事件
 */
public class RemindEvent extends ApplicationEvent implements BaseEvent {

    private final Map<Integer, String> remindMessage;

    public RemindEvent(Map<Integer, String> remindMessage) {
        super(remindMessage);
        this.remindMessage = remindMessage;
    }

    public Map<Integer, String> getRemindMessage() {
        return remindMessage;
    }
}
