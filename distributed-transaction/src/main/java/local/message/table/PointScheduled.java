package local.message.table;

import java.util.List;

public class PointScheduled {

    private PointEventService pointEventService;
    //cron */5 * * * * *
    public void executeEvent() {
        List<Event> eventList = pointEventService.getPublishedEventList();
        if (eventList.isEmpty()) {

        } else {
            //已发表的的积分事件
            for (Event event : eventList) {
                pointEventService.executeEvent(event);
            }
        }

    }
}
