package com.demo.scs.poc.vehicle.service1.domain;

import java.util.Objects;

public class Car {

    private Long id;
    private String vin;
    private String type;
    private String model;
    private String shortModel;

    public Car() {
    }

    public Car(final Car car) {
        this.setId(car.getId()).setVin(car.getVin()).setType(car.getType()).setModel(car.getModel())
                .setShortModel(car.getShortModel());
    }

    public Long getId() {
        return id;
    }

    public Car setId(final Long id) {
        this.id = id;
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
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Car))
            return false;
        final Car car = (Car) o;
        return Objects.equals(id, car.id) && Objects.equals(vin, car.vin) && Objects.equals(type,
            car.type) && Objects.equals(model, car.model) && Objects.equals(shortModel, car.shortModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vin, type, model, shortModel);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Car{");
        sb.append("id=").append(id);
        sb.append(", vin='").append(vin).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", shortModel='").append(shortModel).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
