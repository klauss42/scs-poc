package com.demo.scs.poc.shared.usercontext.service;

import java.util.HashMap;
import java.util.Map;

// Note this doesn't extend ResourceSupport being used for request only
public class Data {

    private String userId;
    private Map<String, String> data = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public Data setUserId(final String userId) {
        this.userId = userId;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Data setData(final Map<String, String> data) {
        this.data = data;
        return this;
    }
}
