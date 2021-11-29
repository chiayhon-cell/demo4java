package cn.chiayhon.dao;

import cn.chiayhon.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    User queryById(Long id);
}
