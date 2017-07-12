package com.demo.scs.poc.connect.ui.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;
import com.demo.scs.poc.commons.pssignal.SignalResponseType;
import com.demo.scs.poc.commons.pssignal.SignalType;
import com.demo.scs.poc.connect.ui.controller.v1.ConnectV1BeatProvider;
import com.demo.scs.poc.connect.ui.service.NavigationService;

@Controller
public class ConnectUiController {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectUiController.class);

    @Value("${spring.application.name}")
    private String serviceId;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private PsSignalUrlService psSignalUrlService;

    @Autowired
    private ConnectV1BeatProvider beatProvider;

    @Timed
    @ExceptionMetered
    @RequestMapping("/user")
    public Principal user(Principal user) {
        LOG.debug("Entering /user ...");
        return user;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/login")
    public String login() {
        LOG.debug("Entering /login ...");
        return "redirect:/";
    }

    @Timed
    @ExceptionMetered
    @RequestMapping({"/", "/index", "/index.html"})
    public ModelAndView index(HttpServletRequest req) {
        LOG.debug("Entering /index ...");
        ModelAndView mv = prepareModelAndView("index", req);

        return mv;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/page1")
    public ModelAndView page1(HttpServletRequest req) {
        LOG.debug("Entering /page1 ...");
        ModelAndView mv = prepareModelAndView("page1", req);

        return mv;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/page2")
    public ModelAndView page2(HttpServletRequest req) {
        LOG.debug("Entering /page2 ...");
        ModelAndView mv = prepareModelAndView("page2", req);
        addNavbar(mv, req);

        return mv;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/page3")
    public ModelAndView page3(HttpServletRequest req) {
        LOG.debug("Entering /page3 ...");
        ModelAndView mv = prepareModelAndView("page3", req);

        return mv;
    }

    private ModelAndView prepareModelAndView(String viewName, HttpServletRequest req) {
        ModelAndView mv = new ModelAndView(viewName);
        addNavbar(mv, req);
        addSignal(mv, viewName);

        return mv;
    }

    private void addNavbar(ModelAndView model, HttpServletRequest req) {
        String navbarFragment = navigationService.getNavbar(req);
        model.addObject("navigationNavbar", navbarFragment);
    }

    private void addSignal(ModelAndView mv, String viewName) {
        String beatId = beatProvider.beatByPageId(viewName);
        String url = psSignalUrlService.builder()
                                       .responseType(SignalResponseType.GIF)
                                       .serviceId(serviceId)
                                       .pageId(viewName)
                                       .beatId(beatId)
                                       .type(SignalType.VIEW)
                                       .build();

        mv.addObject("signalImgUrl", url);
    }
}
