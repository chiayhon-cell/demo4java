package cn.chiayhon.spring.sql;


import cn.chiayhon.pojo.Gender;
import cn.chiayhon.pojo.User;
import cn.chiayhon.rest.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Component
public class SpringTransactionControlDemo {

    @Resource
    private UserMapper userMapper;

    @Transactional
    public void main() {
        update();
    }

    public int update() {

        User user
                = User.builder()
                .id(1L)
                .username("李四123")
                .gender(Gender.WOMAN)
                .build();
        final int i = userMapper.updateById(user);
        if (true) {
            log.info("fail updating");
            throw new RuntimeException();
        }
        return i;
    }

}
