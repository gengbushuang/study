package local.message.table;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class PointEventService {

    private BaseEventDao pointEventDao;

    private PointService pointService;

    public int newEvent(Event event) {
        if (event != null) {
            return pointEventDao.insert(event);
        }
        throw new RuntimeException("");
    }

    public List<Event> getPublishedEventList() {
        return pointEventDao.getByProcess(EventProcess.PUBLISHED.getValue());
    }

    public void executeEvent(Event event) {
        if (event != null) {
            String eventProcess = event.getProcess();
            if (EventProcess.PUBLISHED.getValue().equals(eventProcess)
                    && EventType.NEW_POINT.getValue().equals(event.getType())) {
                //解析字符串
                String json = event.getContent();
                Point point = JSON.parseObject(json, Point.class);
                pointService.newPoint(point);

                event.setProcess(EventProcess.PROCESSED.getValue());
                pointEventDao.updateProcess(event);
            }
        }
    }
}
