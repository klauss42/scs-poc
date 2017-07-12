package com.demo.scs.poc.vehicle.ui.beans;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Car extends ResourceSupport {

    private Long carId;
    private String vin;
    private String type;
    private String model;
    private String shortModel;

    public Long getCarId() {
        return carId;
    }

    public Car setCarId(final Long carId) {
        this.carId = carId;
        return this;
    }

    public String getVin() {
        return vin;
    }

    public Car setVin(final String vin) {
        this.vin = vin;
        return this;
    }

    public String getType() {
        return type;
    }

    public Car setType(final String type) {
        this.type = type;
        return this;
    }

    public String getModel() {
        return model;
    }

    public Car setModel(final String model) {
        this.model = model;
        return this;
    }

    public String getShortModel() {
        return shortModel;
    }

    public Car setShortModel(final String shortModel) {
        this.shortModel = shortModel;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Car{");
        sb.append("carId=").append(carId);
        sb.append(", vin='").append(vin).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", shortModel='").append(shortModel).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
