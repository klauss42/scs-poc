package com.demo.scs.poc.shared.framing.beans;

import java.util.ArrayList;
import java.util.List;

public class NavData {

    public static class Service {

        private String name = "Unnamed";
        private String url = "#";
        private boolean active;
        private boolean disabled;
        private List<Service> subServices;

        public Service() {
        }

        public Service(String name) {
            this.name = name;
        }

        public Service(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public Service setName(String name) {
            this.name = name;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Service setUrl(String url) {
            this.url = url;
            return this;
        }

        public boolean isActive() {
            return active;
        }

        public Service setActive(boolean active) {
            this.active = active;
            return this;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public Service setDisabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Service addSubService(Service subService) {
            if (subServices == null) {
                subServices = new ArrayList<>();
            }
            subServices.add(subService);
            return this;
        }

        public List<Service> getSubServices() {
            return subServices;
        }

        public Service setSubServices(List<Service> subServices) {
            this.subServices = subServices;
            return this;
        }
    }

    private List<Service> services = new ArrayList<>();

    public NavData addService(Service service) {
        services.add(service);
        return this;
    }

    public List<Service> getServices() {
        return services;
    }

    public NavData setServices(List<Service> services) {
        this.services = services;
        return this;
    }
}
