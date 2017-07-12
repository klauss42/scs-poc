package com.demo.scs.poc.commons.pssignal;

public enum SignalResponseType {
    HTML("html"), JAVASCRIPT("js"), GIF("gif");

    private final String paramValue;

    SignalResponseType(String paramValue) {
        this.paramValue = paramValue;
    }

    String getParamValue() {
        return paramValue;
    }
}
