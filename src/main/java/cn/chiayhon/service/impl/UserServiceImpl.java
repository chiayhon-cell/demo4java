package cn.chiayhon.service.impl;

import cn.chiayhon.bean.User;
import cn.chiayhon.dao.UserDao;
import cn.chiayhon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User queryById(Long id) {
        return this.userDao.queryById(id);
    }
}
