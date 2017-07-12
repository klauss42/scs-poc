package com.demo.scs.poc.vehicle.service1.service;

import java.util.List;

import com.demo.scs.poc.vehicle.service1.domain.Car;

public interface CarService {

    Car findById(Long id);

    Car findByVin(String vin);

    Car save(Car car);

    Car update(Car car);

    void deleteById(Long id);

    List<Car> findAll();

    void deleteAll();

    boolean exists(Long id);

    boolean vinExists(String vin);

    boolean exists(Car car);

}
