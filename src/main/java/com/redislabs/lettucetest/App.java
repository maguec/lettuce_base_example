package com.redislabs.lettucetest;

import java.util.List;

import io.lettuce.core.KeyValue;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

public class App {

    private final static int KEY_COUNT = 1000000;

    public static void main(String[] args) throws Exception {
        //RedisClusterClient redisClient = RedisClusterClient.create("redis://35.236.67.152:15989/0");
        RedisClusterClient redisClient = RedisClusterClient.create("redis://localhost:30001/0");
        StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
        System.out.println("Connected to Redis");
        RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
        String[] keys = keys();
        for (String key : keys) {
            syncCommands.set(key, key + "-value");
        }
        List<KeyValue<String, String>> results = syncCommands.mget(keys);
        System.out.println(results.size());
        connection.close();
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
