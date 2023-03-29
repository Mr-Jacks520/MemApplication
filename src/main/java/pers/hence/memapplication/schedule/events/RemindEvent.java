package pers.hence.memapplication.schedule.events;

import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 14:22
 * @description 提醒事件
 */
public class RemindEvent extends ApplicationEvent implements BaseEvent {

    private final Set<String> userIds;

    public RemindEvent(Set<String> userIds) {
        super(userIds);
        this.userIds = userIds;
    }

    public Set<String> getUserIds() {
        return userIds;
    }
}
