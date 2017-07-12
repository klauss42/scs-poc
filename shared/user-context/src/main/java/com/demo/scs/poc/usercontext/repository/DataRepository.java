package com.demo.scs.poc.usercontext.repository;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.demo.scs.poc.usercontext.domain.Data;

@Repository
public class DataRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DataRepository.class);

    @Autowired
    private RedisTemplate<String, Data> redisTemplate;

    public void save(String userId, Data data) {
        LOG.info("Saving user context data for userId {}: {}", userId, data);
        redisTemplate.opsForValue().set(userId, data);
    }

    public Data findById(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void deleteById(String userId) {
        LOG.debug("Deleting user context data for userId {}", userId);
        redisTemplate.delete(userId);
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        LOG.debug("Deleting user context data for userIds {}", keys);
        redisTemplate.delete(keys);
    }
}
