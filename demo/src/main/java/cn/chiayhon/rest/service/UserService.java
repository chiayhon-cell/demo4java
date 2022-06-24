package cn.chiayhon.rest.service;

import cn.chiayhon.pojo.User;

public interface UserService {

    boolean register(User user);

    User getByUsername(String username);
}
