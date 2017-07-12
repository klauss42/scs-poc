package com.demo.scs.poc.home.ui.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PsEngineBeat {

    private String service;
    private String id;

    @Override
    public String toString() {
        return "PsEngineBeat("
               + "service='" + service + '\''
               + ", id='" + id + '\''
               + ')';
    }

    public String getService() {
        return service;
    }

    public PsEngineBeat setService(String service) {
        this.service = service;
        return this;
    }

    public String getId() {
        return id;
    }

    public PsEngineBeat setId(String id) {
        this.id = id;
        return this;
    }
}
