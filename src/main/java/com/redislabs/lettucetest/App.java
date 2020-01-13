package com.redislabs.lettucetest;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

public class App {

    private final static int KEY_COUNT = 100;

    public static void main(String[] args) throws Exception {
        RedisClient client = RedisClient.create(RedisURI.create("redis://test123@redis-10003.azure1.mague.com:10003/0"));
        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig());
        System.out.println("Connection pool to Redis");
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();
            commands.multi();
            String[] keys = keys();
            for (String key : keys) {
                commands.set(key, key + "-value");
            }
            commands.exec();
        }
        pool.close();
        redisClient.shutdown();
    }

    public static String[] keys() {
        String[] keys = new String[KEY_COUNT];
        for (int index = 0; index < KEY_COUNT; index++) {
            keys[index] = "key" + index;
        }
        return keys;
    }

}
