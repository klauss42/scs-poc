package com.demo.scs.poc.vehicle.service1.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.demo.scs.poc.vehicle.service1.api.IndexResource;

@RestController
@RequestMapping("/")
public class IndexController {

    private final IndexResourceAssembler indexResourceAssembler;

    @Autowired
    public IndexController(IndexResourceAssembler indexResourceAssembler) {
        this.indexResourceAssembler = indexResourceAssembler;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<IndexResource> index() {
        return ResponseEntity.ok(indexResourceAssembler.buildIndex());
    }
}
