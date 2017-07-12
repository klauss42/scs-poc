package com.demo.scs.poc.usercontext.api;

import java.util.HashMap;
import java.util.Map;

// Note this doesn't extend ResourceSupport being used for request only
public class NewData {

    private String userId;
    private Map<String, String> data = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public NewData setUserId(final String userId) {
        this.userId = userId;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public NewData setData(final Map<String, String> data) {
        this.data = data;
        return this;
    }
}
