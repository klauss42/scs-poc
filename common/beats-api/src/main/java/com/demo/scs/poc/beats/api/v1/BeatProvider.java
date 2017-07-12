package com.demo.scs.poc.beats.api.v1;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface BeatProvider {

    String BEAT_PATH = "/v1/beat";

    @RequestMapping(BEAT_PATH)
    Beat beat(@RequestParam String id);

    String BEATS_PATH = "/v1/beats";

    @RequestMapping(BEATS_PATH)
    Collection<Beat> beats(@RequestParam Collection<String> ids);
}
