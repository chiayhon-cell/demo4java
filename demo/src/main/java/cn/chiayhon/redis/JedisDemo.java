package cn.chiayhon.redis;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public class JedisDemo {
    public static void main(String[] args) throws IOException {
        new JedisDemo3().testHash();
    }
}

@Slf4j
class JedisDemo1 {

    public static void main() {
        final Jedis client = init("redis", 6379, "1234321", 0);
        final String result = client.set("name", "张三");
        log.info("result={}", result);
        final String name = client.get("name");
        log.info("name={}", name);
        destroy(client);
    }

    private static Jedis init(String host, Integer port, String password, Integer target) {
        final Jedis redis = new Jedis(host, port);
        redis.auth(password);
        redis.select(target);
        return redis;


    }

    private static void destroy(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}

@Slf4j
class JedisDemo2 {
    // 使用Jedis连接池代替直接创建Jedis
    static class JedisFactory {
        private static final JedisPool pool;

        static {
            final JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(8); // 最大连接
            config.setMaxIdle(8); // 最大空闲连接
            config.setMinIdle(0); // 最小空闲连接
            config.setMaxWait(Duration.ofMinutes(1));
            pool = new JedisPool(config, "redis", 6379, 1000, "1234321");
        }

        public static Jedis getClient() {
            return pool.getResource();
        }
    }

    public static void main() {
        final Jedis client = JedisFactory.getClient();
        final String result = client.set("name", "张三");
        log.info("result={}", result);
        final String name = client.get("name");
        log.info("name={}", name);
    }
}

@Slf4j
class JedisDemo3 {

    public void testHash() {
        final Jedis client = JedisDemo2.JedisFactory.getClient();
        client.hset("user:1", "name", "jack");
        final Map<String, String> map = client.hgetAll("user:1");
        System.out.println(map);
        client.close();
    }
}


