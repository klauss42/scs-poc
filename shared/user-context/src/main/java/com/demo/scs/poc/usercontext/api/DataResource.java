package com.demo.scs.poc.usercontext.api;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResource extends ResourceWithEmbeddeds {

    private String userId;
    private Map<String, String> data;

    @JsonCreator
    public DataResource(@JsonProperty("userId") String userId,
                        @JsonProperty("data") Map<String, String> data) {
        this.userId = userId;
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public DataResource setUserId(final String userId) {
        this.userId = userId;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public DataResource setData(final Map<String, String> data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataResource{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        sb.append(", ").append(super.toString());
        return sb.toString();
    }
}
