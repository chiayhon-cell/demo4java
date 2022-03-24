package cn.chiayhon.spring.eventpublisher;

import cn.chiayhon.pojo.User;

public interface UserService {

    boolean register(User user);

    User getByUsername(String username);
}
