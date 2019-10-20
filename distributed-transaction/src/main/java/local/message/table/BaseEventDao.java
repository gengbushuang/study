package local.message.table;

import java.util.ArrayList;
import java.util.List;

//事件表
public class BaseEventDao {

    public Integer insert(Event event) {
        //insert into t_event(type,process,content,create_time,update_time) values (?,?,?,?,?);

        return 1;
    }

    public Integer updateProcess(Event event) {
        //update t_event set process=?,update_time=now() where id=?;

        return 1;
    }

    public List<Event> getByProcess(String process) {
        //select id,type,process,content from t_event where process=?;
        List<Event> result = new ArrayList<>();

        return result;
    }
}
