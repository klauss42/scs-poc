package com.demo.scs.poc.profile.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;

@Controller
public class UiController {

    private static final Logger LOG = LoggerFactory.getLogger(UiController.class);

    @Timed
    @ExceptionMetered
    @RequestMapping({"/", "/index", "/index.html"})
    public String index() {
        LOG.debug("Entering /index ...");
        return "index";
    }
}
