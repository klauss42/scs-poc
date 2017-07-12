package com.demo.scs.poc.usercontext.domain;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Data{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
