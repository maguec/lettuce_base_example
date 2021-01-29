package com.redislabs.lettucetest;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

public class App {

    public static void main(String[] args) throws Exception {
        RedisClient client = RedisClient.create(RedisURI.create("redis://smally@redis-16031.mague.demo-azure.redislabs.com:16031/0"));
        GenericObjectPool<StatefulRedisConnection<String, String>> pool =
            ConnectionPoolSupport.createGenericObjectPool(client::connect, new GenericObjectPoolConfig());
        System.out.println("Connection pool to Redis");
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisAsyncCommands<String, String> commands = connection.async();
            commands.setAutoFlushCommands(false);
            RedisFuture<String> future1 = commands.set("Foo", "BAR");
            RedisFuture<String> future2 = commands.set("Foo2", "BAR2");
            commands.flushCommands();
            System.out.println(future1.get());
            future2.get();
            
        }
        pool.close();
    }

}
