package com.demo.scs.poc.beats.api.v1;

public class Beat {

    private String id;

    private String template;

    // json of some object
    private String data;

    public Beat() {
    }

    public Beat(String id, String template, String data) {
        this.id = id;
        this.template = template;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Beat("
               + "id='" + id + '\''
               + ", template='" + template + '\''
               + ", data=\'" + data + '\''
               + ')';
    }

    public String getId() {
        return id;
    }

    public Beat setId(String id) {
        this.id = id;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public Beat setTemplate(String template) {
        this.template = template;
        return this;
    }

    public String getData() {
        return data;
    }

    public Beat setData(String data) {
        this.data = data;
        return this;
    }
}
