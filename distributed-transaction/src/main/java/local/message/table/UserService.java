package local.message.table;

import com.alibaba.fastjson.JSON;

public class UserService {

    private UserDao userDao;

    private UserEventService userEventService;

    //要开启事务
    public void newUser(String userName, Integer pointAmount) {
        //保存用户
        String userId = userDao.insert(userName);

        //新增事件
        Event event = new Event();
        event.setType(EventType.NEW_USER.getValue());
        event.setProcess(EventProcess.NEW.getValue());

        Point point = new Point();
        point.setUserId(userId);
        point.setAmount(pointAmount);
        //point json
        String json = JSON.toJSONString(point);
        event.setContent(json);

        userEventService.newEvent(event);
    }
}
