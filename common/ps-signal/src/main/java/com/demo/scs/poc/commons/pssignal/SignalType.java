package com.demo.scs.poc.commons.pssignal;

public enum SignalType {

    VIEW("view"), CLICK("click"), DISPLAY("display");

    private final String paramValue;

    SignalType(String paramValue) {
        this.paramValue = paramValue;
    }

    String getParamValue() {
        return paramValue;
    }
}
