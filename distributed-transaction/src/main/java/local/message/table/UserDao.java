package local.message.table;

import java.util.UUID;

//用户表DAO
public class UserDao {

    public String insert(String userName) {
        String id = UUID.randomUUID().toString().replace("-", "");

        //insert into t_user(id,user_name) values(?,?);
        User u = new User();
        u.setId(id);
        u.setUserName(userName);

        return id;
    }
}
