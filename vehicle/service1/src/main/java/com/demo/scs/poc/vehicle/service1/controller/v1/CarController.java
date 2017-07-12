package com.demo.scs.poc.vehicle.service1.controller.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.demo.scs.poc.vehicle.service1.api.CarResource;
import com.demo.scs.poc.vehicle.service1.api.NewCar;
import com.demo.scs.poc.vehicle.service1.domain.Car;
import com.demo.scs.poc.vehicle.service1.service.CarService;

@RestController
@ExposesResourceFor(CarResource.class)
@RequestMapping("/car")
public class CarController {
    private final Logger LOG = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;
    private final CarResourceAssembler carResourceAssembler;

    @Autowired
    public CarController(final CarService carService, final CarResourceAssembler carResourceAssembler) {
        this.carService = carService;
        this.carResourceAssembler = carResourceAssembler;
    }

    //-------------------Retrieve All Cars--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<CarResource>> listAllCars() {
        LOG.info("Fetching all Cars");
        List<Car> cars = carService.findAll();
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }

        // assemble resources for response
        final Resources<CarResource> wrapped = carResourceAssembler.toEmbeddedList(cars);
        LOG.info("returning resource {}", wrapped);
        return ResponseEntity.ok(wrapped);
    }

    //-------------------Retrieve Single Car--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CarResource> showCar(@PathVariable("id") final long id) {
        LOG.info("Fetching Car with id ‘{}'", id);
        Car car = carService.findById(id);
        if (car == null) {
            LOG.info("Car with id '{}' not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // assemble resources for response
        final CarResource resource = carResourceAssembler.toResource(car);
        LOG.info("returning resource {}", resource);
        return ResponseEntity.ok(resource);
    }

    //-------------------Create a Car--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> newCar(@RequestBody NewCar newCar) {
        LOG.info("Creating Car with VIN '{}'", newCar.getVin());

        if (carService.vinExists(newCar.getVin())) {
            LOG.info("A Car with VIN '{}' already exists", newCar.getVin());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // store new Car
        final Car savedCar = carService.save(carResourceAssembler.toEntity(newCar));

        // assemble resources for response
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", carResourceAssembler.linkToSingleResource(savedCar).getHref());
        LOG.info("returning headers {}", headers);
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //-------------------Update a Car--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCar(@PathVariable("id") long id, @RequestBody NewCar newCar) {
        LOG.info("Updating Car '{}'", id);

        if (! carService.exists(id)) {
            LOG.info("Car with id '{}' not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // update Car
        final Car savedCar = carService.update(carResourceAssembler.toEntity(newCar));
        savedCar.setId(id);

        // assemble resources for response
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", carResourceAssembler.linkToSingleResource(savedCar).getHref());
        LOG.info("returning headers {}", headers);
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }

    //-------------------Delete a Car--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Car> deleteCar(@PathVariable("id") long id) {
        LOG.info("Fetching & Deleting Car with id '{}'", id);

        if (!carService.exists(id)) {
            LOG.info("Unable to delete. Car with id '{}' not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        carService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //-------------------Delete all Cars--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Car> deleteAllCars() {
        LOG.info("Deleting All Cars");

        carService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
