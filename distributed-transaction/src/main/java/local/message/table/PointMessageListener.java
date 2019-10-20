package local.message.table;

public class PointMessageListener implements MessageListener {

    private PointEventService pointEventService;

    @Override
    public void onMessage(String message) {
        //监听器收到消息

        Event event = new Event();
        event.setType(EventType.NEW_POINT.getValue());
        event.setProcess(EventProcess.PUBLISHED.getValue());
        event.setContent(message);

        pointEventService.executeEvent(event);
    }
}
