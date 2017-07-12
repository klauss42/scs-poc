package com.demo.scs.poc.vehicle.service1.api;

// Note this doesn't extend ResourceSupport being used for request only
public class NewCar {

    private String vin;
    private String type;
    private String model;
    private String shortModel;

    public String getVin() {
        return vin;
    }

    public NewCar setVin(final String vin) {
        this.vin = vin;
        return this;
    }

    public String getType() {
        return type;
    }

    public NewCar setType(final String type) {
        this.type = type;
        return this;
    }

    public String getModel() {
        return model;
    }

    public NewCar setModel(final String model) {
        this.model = model;
        return this;
    }

    public String getShortModel() {
        return shortModel;
    }

    public NewCar setShortModel(final String shortModel) {
        this.shortModel = shortModel;
        return this;
    }
}
