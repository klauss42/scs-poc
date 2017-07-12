package com.demo.scs.poc.home.ui.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler
    public void exceptionHandler(Exception e) {
        LOG.error(e.getMessage(), e);
    }

}
