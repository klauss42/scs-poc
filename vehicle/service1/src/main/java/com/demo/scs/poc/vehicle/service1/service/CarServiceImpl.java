package com.demo.scs.poc.vehicle.service1.service;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.scs.poc.vehicle.service1.domain.Car;

@Service("carService")
public class CarServiceImpl implements CarService {

    private final Logger LOG = LoggerFactory.getLogger(CarServiceImpl.class);

    private final static Map<Long, Car> DB = new HashMap<>();
    private static final AtomicLong COUNTER = new AtomicLong();

    static {
        loadCar(new Car().setId(COUNTER.incrementAndGet()).setType("Q5").setModel("Q5 2.0 TDI Ultra 110kW").setShortModel("Q5 2.0 TDI Ultra"));
        loadCar(new Car().setId(COUNTER.incrementAndGet()).setType("Q5").setModel("Q5 2.0 TDI 110kW Quattro").setShortModel("Q5 2.0 TDI"));
        loadCar(new Car().setId(COUNTER.incrementAndGet()).setType("Q5").setModel("Q5 2.0 TFSI 132kW Quattro").setShortModel("Q5 2.0 TFSI"));
    }

    static void loadCar(Car car) {
        DB.put(car.getId(), car);
    }

    @Override
    public List<Car> findAll() {
        return DB.values().stream().collect(toList());
    }

    @Override
    public Car findById(Long id) {
        return DB.get(id);
    }

    @Override
    public Car findByVin(final String vin) {
        for (Car car : DB.values()) {
            if (car.getVin().equals(vin))
                return car;
        }
        return null;
    }

    @Override
    public Car save(Car car) {
        Long id = car.getId() != null ? car.getId() : COUNTER.incrementAndGet();
        DB.put(id, car);
        return car.setId(id);
    }

    @Override
    public Car update(Car car) {
        DB.put(car.getId(), car);
        return car;
    }

    @Override
    public void deleteById(Long id) {
        DB.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return findById(id) != null;
    }

    @Override
    public boolean vinExists(final String vin) {
        return findByVin(vin) != null;
    }

    @Override
    public boolean exists(Car car) {
        return findById(car.getId()) != null;
    }

    @Override
    public void deleteAll() {
        DB.clear();
    }

}
