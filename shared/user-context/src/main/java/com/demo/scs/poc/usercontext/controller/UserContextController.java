package com.demo.scs.poc.usercontext.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
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
import com.demo.scs.poc.usercontext.api.DataResource;
import com.demo.scs.poc.usercontext.api.NewData;
import com.demo.scs.poc.usercontext.domain.Data;
import com.demo.scs.poc.usercontext.repository.DataRepository;

@RestController
@ExposesResourceFor(DataResource.class)
@RequestMapping("/user")
public class UserContextController {
    private final Logger LOG = LoggerFactory.getLogger(UserContextController.class);

    private DataRepository repository;
    private final DataResourceAssembler dataResourceAssembler;

    @Autowired
    public UserContextController(final DataRepository repository, final DataResourceAssembler dataResourceAssembler) {
        this.repository = repository;
        this.dataResourceAssembler = dataResourceAssembler;
    }

    //-------------------Retrieve Single Data--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<DataResource> getUserContext(@PathVariable("userId") final String userId) {
        LOG.info("Fetching User Context data for userId ‘{}'", userId);
        Data data = repository.findById(userId);
        if (data == null) {
            LOG.info("No user context found for userId '{}'", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // assemble resources for response
        final DataResource resource = dataResourceAssembler.toResource(data);
        LOG.info("returning resource {}", resource);
        return ResponseEntity.ok(resource);
    }

    //-------------------Create new UserContext -------------------------------------------------------

    public HttpHeaders createOrUpdateData(String userId, NewData data) {
        // ensure userId is set properly
        data.setUserId(userId);
        // store new Data
        final Data savedData = dataResourceAssembler.toEntity(data);
        repository.save(userId, savedData);

        // assemble resources for response
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", dataResourceAssembler.linkToSingleResource(savedData).getHref());
        LOG.info("returning headers {}", headers);
        return headers;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping(path = "/{userId}", method = RequestMethod.POST)
    public ResponseEntity<Void> newUserContext(@PathVariable String userId, @RequestBody NewData data) {
        LOG.info("Creating User Context data for userId '{}'", userId);
        final HttpHeaders headers = createOrUpdateData(userId, data);
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //-------------------Update a UserContext -------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserContext(@PathVariable String userId, @RequestBody NewData data) {
        LOG.info("Updating User Context data for userId '{}'", userId);
        final HttpHeaders headers = createOrUpdateData(userId, data);
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }

    //-------------------Delete a Data--------------------------------------------------------

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Data> deleteUserContext(@PathVariable("userId") String userId) {
        LOG.info("Deleting User Context data for userId '{}'", userId);
        repository.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
