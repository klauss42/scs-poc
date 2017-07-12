package com.demo.scs.poc.usercontext;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.embedded.RedisServer;

@Component
public class EmbeddedRedis {

    @Value("${redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder().setting("bind 127.0.0.1").port(redisPort).build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
