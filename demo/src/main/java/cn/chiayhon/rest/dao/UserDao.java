package cn.chiayhon.rest.dao;

import cn.chiayhon.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    /**
     * query user by primary id
     *
     * @param userId
     * @return
     */
    User getUserById(long userId);
}
