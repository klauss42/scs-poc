package com.demo.scs.poc.usercontext.conf;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.demo.scs.poc.usercontext.domain.Data;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @PostConstruct
    public void postConstruct() {
        LOG.debug("Creating redis connector to {}:{}", redisHost, redisPort);
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool props = null;
        config.setMaxTotal(props.getMaxActive());
        config.setMaxIdle(props.getMaxIdle());
        config.setMinIdle(props.getMinIdle());
        config.setMaxWaitMillis(props.getMaxWait());

        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setUsePool(false);
//        factory.setPoolConfig(jedisPoolConfig());

        return factory;
    }

    @Bean
    public RedisTemplate<String, Data> redisTemplate() {
        RedisTemplate<String, Data> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Data>(Data.class));

        return redisTemplate;
    }
}
