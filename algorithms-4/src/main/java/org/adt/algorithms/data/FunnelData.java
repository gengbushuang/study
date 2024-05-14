package org.adt.algorithms.data;

/**
 * @author gengbushuang
 * @date 2024/5/14 10:28
 */
public class FunnelData {
    private int userId;

    private String event;

    private String day;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", event='" + event + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public static FunnelData create(int userId, String event, String day){
        FunnelData funnelData = new FunnelData();
        funnelData.setDay(day);
        funnelData.setEvent(event);
        funnelData.setUserId(userId);

        return funnelData;
    }
}
