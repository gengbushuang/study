package local.message.table;

import java.util.List;

public class UserScheduled {

    private UserEventService userEventService;

    //cron */5 * * * * *
    public void executeEvent() {
        List<Event> eventList = userEventService.getNewEventList();
        if (eventList.isEmpty()) {

        } else {
            //新建用户的事件记录数
            for (Event event : eventList) {
                userEventService.executeEvent(event);
            }
        }
    }
}
