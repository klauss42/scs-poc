package com.demo.scs.poc.home.ui.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.demo.scs.poc.beats.api.v1.Beat;
import com.demo.scs.poc.beats.api.v1.BeatProvider;
import com.demo.scs.poc.home.ui.beans.PsEngineBeat;

@Component
public class BeatProviderService {

    private static final Logger LOG = LoggerFactory.getLogger(BeatProviderService.class);

//    @Autowired
//    @Qualifier("clientCredentialsRestTemplate")
//    private RestTemplate restTemplate;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "queryProviderForBeatFallback")
    public Optional<Beat> queryProviderForBeat(String serviceUrl, PsEngineBeat psEngineBeat) {
        String url = serviceUrl + BeatProvider.BEAT_PATH + "?id=" + psEngineBeat.getService() + ':' + psEngineBeat.getId();
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not create uri of '" + url + "'", e);
        }
        Beat beat = restTemplate.getForObject(uri, Beat.class);
        if (beat == null) {
            LOG.warn("Null result from beat provider " + psEngineBeat.getService() + " for beat " + psEngineBeat.getId());
            return Optional.empty();
        }
        LOG.debug("Resolved beat for {}: {}", psEngineBeat, beat);

        return Optional.of(beat);
    }

    @SuppressWarnings("unused")
    Optional<Beat> queryProviderForBeatFallback(String url, PsEngineBeat psEngineBeat, Throwable e) {
        LOG.warn("Could not query beat provider " + psEngineBeat.getService() + " for beat " + psEngineBeat.getId(), e);
        return Optional.empty();
    }
}
