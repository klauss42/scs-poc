package com.demo.scs.poc.home.beat.render.service;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinkExpander {

    private static final Logger LOG = LoggerFactory.getLogger(LinkExpander.class);

    @Value("${scspoc.gateway.url}")
    private String gatewayUrl;

    @PostConstruct
    public void postConstruct() {
        // ensure trailing slash
        if (gatewayUrl.charAt(gatewayUrl.length() - 1) != '/') {
            gatewayUrl = gatewayUrl + '/';
        }
    }

    public String expandLink(String link) {
        if (link == null || link.startsWith("http")) {
            return link;
        }
        // links are build by serviceId + path (without context), zuul uses this as path pattern so href building becomes simple
        // TODO check if service is available using ConsulServiceLocator?
        final StringBuilder urlSb = new StringBuilder(gatewayUrl);
        if (link.charAt(0) == '/') {
            urlSb.append(link, 1, link.length());
        } else {
            urlSb.append(link);
        }
        return urlSb.toString();
    }

    public void expandLink(String link, Consumer<String> consumer) {
        String expandedLink = expandLink(link);
        consumer.accept(expandedLink);
    }
}
