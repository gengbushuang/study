package local.message.table;

import java.util.UUID;

//积分表
public class PointDao {

    public String insert(Point point) {
        String id = UUID.randomUUID().toString().replace("-", "");
        point.setId(id);
        //insert into t_point(id,user_id,amount) values(?,?,?);

        return id;
    }
}
