package cn.chiayhon.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringDataRedisDemo implements ApplicationRunner {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
