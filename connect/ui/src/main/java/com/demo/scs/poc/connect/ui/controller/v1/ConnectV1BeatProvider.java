package com.demo.scs.poc.connect.ui.controller.v1;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.scs.poc.beats.api.v1.Beat;
import com.demo.scs.poc.beats.api.v1.BeatProvider;

@RestController
public class ConnectV1BeatProvider implements BeatProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectV1BeatProvider.class);

    // TODO use beat catalog
    private static final Map<String, String> PAGE_BEAT_MAP;
    static {
        HashMap<String, String> map = new HashMap<>();
        map.put("page1", "connect:1");
        map.put("page2", "connect:2");
        map.put("page3", "connect:3");

        PAGE_BEAT_MAP = map;
    }

    private static final Map<String, Beat> BEATS = Stream.of(
        new Beat("connect:1", "template1", "{\"text\": \"Connect Data 1\", \"link\": \"connect-ui/page1\"}"),
        new Beat("connect:2", "template2", "{\"text\": \"Connect Data 2\", \"link\": \"connect-ui/page2\"}"),
        new Beat("connect:3", "template3", "{\"text\": \"Connect Data 3\", \"link\": \"connect-ui/page3\"}")
                                                            ).collect(Collectors.toMap(Beat::getId, Function.identity()));

    public String beatByPageId(String pageId) {
        return PAGE_BEAT_MAP.get(pageId);
    }

    @Override
    public Beat beat(@RequestParam String id) {
        LOG.debug("Creating v1 beat for id {}", id);
        return BEATS.get(id);
    }

    @Override
    public Collection<Beat> beats(@RequestParam Collection<String> ids) {
        LOG.debug("Creating v1 beats for ids {}", ids);
        if (ids.size() == 0) {
            return Collections.emptyList();
        }
        Set<String> set = new HashSet<>(ids);
        List<Beat> result = BEATS.values().stream()
                                 .filter(b -> set.contains(b.getId()))
                                 .collect(Collectors.toList());

        return result;
    }
}
