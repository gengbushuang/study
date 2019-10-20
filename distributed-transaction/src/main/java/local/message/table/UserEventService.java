package local.message.table;

import java.util.List;

public class UserEventService {

    private BaseEventDao userEventDao;

    public int newEvent(Event event) {
        if (event != null) {
            return userEventDao.insert(event);
        }
        throw new RuntimeException("");
    }

    public List<Event> getNewEventList() {
        return userEventDao.getByProcess(EventProcess.NEW.getValue());
    }

    public void executeEvent(Event event) {
        if (event != null) {
            String eventProcess = event.getProcess();
            if (EventProcess.NEW.getValue().equals(eventProcess)
                    && EventType.NEW_USER.getValue().equals(event.getType())) {
                //发送消息到队列里面
                String content = event.getContent();
                //消息转换状态new->published
                event.setProcess(EventProcess.PUBLISHED.getValue());
                userEventDao.updateProcess(event);
            }
        }
    }
}
