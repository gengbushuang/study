package local.message.table;

public enum EventType {

    NEW_USER("NEW_USER","新增用戶"),
    NEW_POINT("NEW_POINT","新增积分");

    private String value;

    private String desc;

    EventType(String value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
