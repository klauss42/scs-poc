package com.demo.scs.poc.vehicle.ui.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;
import com.demo.scs.poc.commons.pssignal.SignalResponseType;
import com.demo.scs.poc.commons.pssignal.SignalType;
import com.demo.scs.poc.nav.fallback.NavFallback;
import com.demo.scs.poc.shared.usercontext.service.UserContextService;
import com.demo.scs.poc.vehicle.ui.beans.Car;
import com.demo.scs.poc.vehicle.ui.controller.v1.VehicleV1BeatProvider;
import com.demo.scs.poc.vehicle.ui.service.CarService;
import com.demo.scs.poc.vehicle.ui.service.NavigationService;

@Controller
public class VehicleUiController {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleUiController.class);

    @Value("${spring.application.name}")
    private String serviceId;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private CarService carService;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NavFallback navFallback;

    @Autowired
    private PsSignalUrlService psSignalUrlService;

    @Autowired
    private VehicleV1BeatProvider beatProvider;

    @Timed
    @ExceptionMetered
    @RequestMapping({"/", "/page1"})
    public ModelAndView page1(HttpServletRequest req, Principal user) {
        LOG.debug("Entering / or /page1 ...");
        ModelAndView mv = prepareModelAndView("page1", req);

        Car currentCar = getCarFromUserContext(user.getName());
        if (currentCar != null) {
            mv.addObject("selectedCar", currentCar);
            mv.addObject("carIsSelected", true);
        } else {
            mv.addObject("carList", carService.findAll());
            mv.addObject("selectedCar", new Car());
            mv.addObject("carIsSelected", false);
        }
        return mv;
    }

    @Timed
    @ExceptionMetered
    @PostMapping({"/", "/page1"})
    public ModelAndView selectCar(@ModelAttribute Car car, HttpServletRequest req, Principal user) {
        ModelAndView mv = new ModelAndView("page1");
        addNavbar(mv, req);

        if (car != null && car.getCarId() != null) {
            car = carService.findById(car.getCarId());
            putCarIntoUserContext(user.getName(), car);
        }
        mv.addObject("selectedCar", car);
        mv.addObject("carIsSelected", true);
        return mv;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/page2")
    public ModelAndView page2(HttpServletRequest req) {
        LOG.debug("Entering /page2 ...");
        ModelAndView mv = prepareModelAndView("page2", req);

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

    private Car getCarFromUserContext(String userId) {
        Car car = null;
        Map<String, String> uc = userContextService.getContext(userId);
        if (uc == null) {
            uc = new HashMap<>();
        }
        try {
            String json = uc.get("selectedCar");
            if (json != null) {
                car = objectMapper.readValue(json, Car.class);
            }
        } catch (IOException e) {
            LOG.error("Error while deserializing Car JSON", e);
        }
        return car;
    }

    private void putCarIntoUserContext(String userId, Car car) {
        Map<String, String> uc = userContextService.getContext(userId);
        if (uc == null) {
            uc = new HashMap<>();
        }
        try {
            String json = objectMapper.writeValueAsString(car);
            uc.put("selectedCar", json);
            userContextService.putContext(userId, uc);
        } catch (IOException e) {
            LOG.error("Error while serializing Car JSON", e);
        }
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
