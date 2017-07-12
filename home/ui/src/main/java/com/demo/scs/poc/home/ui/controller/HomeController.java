package com.demo.scs.poc.home.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.demo.scs.poc.beats.api.v1.Beat;
import com.demo.scs.poc.commons.consul.ConsulServiceLocator;
import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;
import com.demo.scs.poc.home.ui.HomeUIConstants;
import com.demo.scs.poc.home.ui.beans.PsEngineBeat;
import com.demo.scs.poc.home.ui.service.BeatLocator;
import com.demo.scs.poc.home.ui.service.BeatProviderService;
import com.demo.scs.poc.home.ui.service.BeatRenderService;
import com.demo.scs.poc.home.ui.service.NavigationService;
import com.demo.scs.poc.home.ui.service.PsEngineService;

@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Value("${spring.application.name}")
    private String serviceId;

    @Autowired
    private ConsulServiceLocator consulServiceLocator;

    @Autowired
    private BeatLocator beatLocator;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private PsEngineService psEngineService;

    @Autowired
    private BeatRenderService beatRenderService;

    @Autowired
    private BeatProviderService beatProviderService;

    @Autowired
    private PsSignalUrlService psSignalUrlService;


    @Timed
    @ExceptionMetered
    @RequestMapping({ "/", "/index", "/index.html" })
    public String index(HttpServletRequest req,
                        Model model,
                        @RequestParam(required = false) String userId,
                        @CookieValue(name = HomeUIConstants.PSEID_COOKIE, required = false) String pseIdCookie) {
        LOG.debug("Entering /index ...");
        final String viewName = "index";

        if(userId == null && pseIdCookie != null) {
            userId = pseIdCookie;
            LOG.debug("Using Cookie pse-id '" + userId + "");
        }

        addNavbar(model, req);
        addBeats(model, userId, viewName);

        return viewName;
    }

    private void addNavbar(Model model, HttpServletRequest req) {
        String naviHtml = navigationService.getNavbar(req);
        model.addAttribute("navigationNavbar", naviHtml);
    }

    private void addBeats(Model model, String userId, String pageId) {
        Map<String, String> availableBeatProviders = beatLocator.availableBeatProviders();
        LOG.debug("Available beat providers: {}", availableBeatProviders);
        if (availableBeatProviders.size() == 0) {
            LOG.info("No beat provider are currently available.");
            model.addAttribute("beats", Collections.emptyList());
            return;
        }

        List<PsEngineBeat> psEngineBeats = psEngineService.queryBeatsFor(userId);
        LOG.debug("Ps-engine beats for user {}: {}", userId, psEngineBeats);
        if (psEngineBeats.size() == 0) {
            LOG.info("No beats from ps-engine for user {}.", userId);
            model.addAttribute("beats", Collections.emptyList());
            return;
        }

        final String renderServiceId = "home-beats-render-service";
        Optional<String> optionalRenderServiceUrl = consulServiceLocator.discoverServiceUrl(renderServiceId);
        if (!optionalRenderServiceUrl.isPresent()) {
            LOG.warn("Could not get service url for " + renderServiceId);
            model.addAttribute("beats", Collections.emptyList());
            return;
        }

        Map<String, String> serviceUrls = discoverServiceUrlsByBeatname(psEngineBeats, availableBeatProviders);

        List<Beat> beats = new ArrayList<>();
        for (PsEngineBeat psEngineBeat : psEngineBeats) {
            String serviceUrl = serviceUrls.get(psEngineBeat.getService());
            if (serviceUrl == null) {
                LOG.info("Could not use beat {} of service {} for user {} since provider is not available", psEngineBeat.getId(),
                         psEngineBeat.getService(), userId);
                continue;
            }
            beatProviderService.queryProviderForBeat(serviceUrl, psEngineBeat).ifPresent(beats::add);
        }

        String renderServiceUrl = optionalRenderServiceUrl.get();
        List<String> beatsAsHtml = new ArrayList<>();
        for (Beat beat : beats) {
            beatRenderService.renderBeat(renderServiceUrl, beat, pageId).ifPresent(beatsAsHtml::add);
        }

        model.addAttribute("beats", beatsAsHtml);
    }

    private Map<String, String> discoverServiceUrlsByBeatname(Collection<PsEngineBeat> psEngineBeats,
            Map<String, String> availableBeatProviders) {

        HashMap<String, String> cache = new HashMap<>();
        HashMap<String, String> result = new HashMap<>();
        for (PsEngineBeat psEngineBeat : psEngineBeats) {
            String serviceName = psEngineBeat.getService();
            String serviceId = availableBeatProviders.get(serviceName);
            Optional<String> optionalUrl = consulServiceLocator.discoverServiceUrl(serviceId, cache);
            if (optionalUrl.isPresent()) {
                result.put(serviceName, optionalUrl.get());
            }
        }
        return result;
    }
}
