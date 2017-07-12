package com.demo.scs.poc.shared.framing.nav.v1;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.demo.scs.poc.shared.framing.beans.NavData;

@Controller
@RequestMapping("/v1")
public class NavigationController {

    private static final Logger LOG = LoggerFactory.getLogger(NavigationController.class);

    @Value("${scspoc.gateway.url}")
    private String gatewayUrl;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostConstruct
    public void postConstruct() {
        LOG.debug("Constructed: scspoc.gateway.url={}", gatewayUrl);
    }

    @Timed
    @ExceptionMetered
    @RequestMapping({"/", "/index", "/index.html"})
    public String index(Model model, @RequestParam(required = false) String current) {
        LOG.debug("Entering index ...");
        model.addAttribute("navdata", createNavData(current));
        return "navigation/v1/index";
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/navbar")
    public String navbar(Model model, @RequestParam(required = false) String current) {
        LOG.debug("Entering navbar ...");
        model.addAttribute("navdata", createNavData(current));
        return "navigation/v1/navbar";
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/navbardata")
    public NavData navbarData(@RequestParam(required = false) String current) {
        LOG.debug("Entering navbardata ...");
        return createNavData(current);
    }

    @Timed
    @ExceptionMetered
    @RequestMapping("/head_bootstrap")
    public String head() {
        LOG.debug("Entering head_bootstrap ...");
        return "navigation/v1/head_bootstrap";
    }

    private NavData createNavData(String current) {
        // TODO: lookup nav providers via service discovery?
        NavData navData = new NavData();
        addHomeNavData(navData, current);
        addConnectNavData(navData, current);
        addVehicleNavData(navData, current);

        return navData;
    }

    private static final String SERVICE_ID_HOME_UI = "home-ui";
    private static final String GATEWAY_PATH_HOME_UI = "/" + SERVICE_ID_HOME_UI;

    private void addHomeNavData(NavData navData, String current) {
        String homeUrl = gatewayUrl + GATEWAY_PATH_HOME_UI;
        boolean active = current == null || current.length() == 0 || "home-ui".equals(current);
        List<ServiceInstance> services = discoveryClient.getInstances(SERVICE_ID_HOME_UI);
        boolean disabled = services == null || services.size() == 0;

        navData.addService(new NavData.Service("Home", homeUrl).setActive(active).setDisabled(disabled));
    }

    private static final String SERVICE_ID_CONNECT_UI = "connect-ui";
    private static final String GATWAY_PATH_CONNECT_UI = "/" + SERVICE_ID_CONNECT_UI;

    private void addConnectNavData(NavData navData, String current) {
        String connectUrl = gatewayUrl + GATWAY_PATH_CONNECT_UI;
        boolean active = SERVICE_ID_CONNECT_UI.equals(current);
        List<ServiceInstance> services = discoveryClient.getInstances(SERVICE_ID_CONNECT_UI);
        boolean disabled = services == null || services.size() == 0;

        navData.addService(new NavData.Service("Connect", connectUrl).setActive(active).setDisabled(disabled));
    }

    private static final String SERVICE_ID_VEHICLE_UI = "vehicle-ui";
    private static final String GATWAY_PATH_VEHICLE_UI = "/" + SERVICE_ID_VEHICLE_UI;

    private void addVehicleNavData(NavData navData, String current) {
        String vehicleUrl = gatewayUrl + GATWAY_PATH_VEHICLE_UI;
        boolean active = SERVICE_ID_VEHICLE_UI.equals(current);
        List<ServiceInstance> services = discoveryClient.getInstances(SERVICE_ID_VEHICLE_UI);
        boolean disabled = services == null || services.size() == 0;

        navData.addService(new NavData.Service("Vehicle")
                                   .setActive(active).setDisabled(disabled)
                                   .addSubService(new NavData.Service("Vehicle 1", vehicleUrl + "/page1")
                                                          .setActive(active)
                                                          .setDisabled(disabled))
                                   .addSubService(new NavData.Service("Vehicle 2", vehicleUrl + "/page2")
                                                          .setActive(active)
                                                          .setDisabled(disabled))
                                   .addSubService(new NavData.Service("Vehicle 3", vehicleUrl + "/page3")
                                                          .setActive(active)
                                                          .setDisabled(disabled)));
    }
}
