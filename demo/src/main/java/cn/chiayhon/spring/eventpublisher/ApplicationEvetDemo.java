package cn.chiayhon.spring.eventpublisher;

import cn.chiayhon.pojo.User;
import cn.chiayhon.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEvetDemo {

    @Autowired
    private UserService userService;

    public boolean register(User user) {
        return userService.register(user);
    }
}
