package pers.hence.memapplication.schedule.events;

import org.springframework.context.ApplicationEvent;
import pers.hence.memapplication.model.vo.MemContentVO;

import java.util.List;
import java.util.Map;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 13:50
 * @description 预热事件
 */
public class PreCachedEvent extends ApplicationEvent implements BaseEvent{

    private final Map<Integer, List<MemContentVO>> task;

    public PreCachedEvent(Map<Integer, List<MemContentVO>> task) {
        super(task);
        this.task = task;
    }

    public Map<Integer, List<MemContentVO>> getTask() {
        return task;
    }
}
