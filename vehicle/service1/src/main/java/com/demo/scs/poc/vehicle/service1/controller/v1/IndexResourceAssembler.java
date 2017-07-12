package com.demo.scs.poc.vehicle.service1.controller.v1;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Service;

import com.demo.scs.poc.vehicle.service1.api.CarResource;
import com.demo.scs.poc.vehicle.service1.api.IndexResource;

@Service
public class IndexResourceAssembler {

    private final RelProvider relProvider;
    private final EntityLinks entityLinks;

    @Autowired
    public IndexResourceAssembler(RelProvider relProvider, EntityLinks entityLinks) {
        this.relProvider = relProvider;
        this.entityLinks = entityLinks;
    }

    public IndexResource buildIndex() {
        // Note this is unfortunately hand-written. If you add a new entity, have to manually add a new link
        final List<Link> links = asList(entityLinks.linkToCollectionResource(CarResource.class)
            .withRel(relProvider.getCollectionResourceRelFor(CarResource.class)));
        final IndexResource resource = new IndexResource("vehicle-service1", "Vehicle Service API");
        resource.add(links);
        return resource;
    }
}
