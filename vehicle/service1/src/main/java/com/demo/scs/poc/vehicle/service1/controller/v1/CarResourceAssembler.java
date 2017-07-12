package com.demo.scs.poc.vehicle.service1.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Service;

import com.demo.scs.poc.vehicle.service1.api.CarResource;
import com.demo.scs.poc.vehicle.service1.api.NewCar;
import com.demo.scs.poc.vehicle.service1.domain.Car;

@Service
public class CarResourceAssembler extends EmbeddableResourceAssemblerSupport<Car, CarResource, CarController>{


   @Autowired
   public CarResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
      super(entityLinks, relProvider, CarController.class, CarResource.class);
   }

   @Override
   public Link linkToSingleResource(Car beat) {
       return entityLinks.linkToSingleResource(CarResource.class, beat.getId());
   }


   @Override
   public CarResource toResource(Car entity) {
      final CarResource resource = createResourceWithId(entity.getId(), entity);
      return resource;
   }

   @Override
   protected CarResource instantiateResource(Car entity) {
      return new CarResource(
               entity.getId(),
               entity.getVin(),
               entity.getType(),
               entity.getModel(),
               entity.getShortModel());
   }

   public Car toEntity(NewCar newCar) {
       return new Car()
               .setVin(newCar.getVin())
               .setType(newCar.getType())
               .setModel(newCar.getModel())
               .setShortModel(newCar.getShortModel());
   }
}

