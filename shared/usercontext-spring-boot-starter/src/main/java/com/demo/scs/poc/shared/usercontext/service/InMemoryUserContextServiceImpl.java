package com.demo.scs.poc.shared.usercontext.service;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserContextServiceImpl implements UserContextService {

    private Map<String, Map<String, String>> data = new HashMap<>();

    @Override
    public Map<String, String> getContext(String userId) {
        Map<String, String> uc = data.get(userId);
        if (uc == null) {
            uc = new HashMap<>();
        }
        return uc;
    }

    @Override
    public void putContext(String userId, Map<String, String> map) {
        data.put(userId, map);
    }
}
