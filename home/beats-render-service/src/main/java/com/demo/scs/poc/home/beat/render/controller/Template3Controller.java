package com.demo.scs.poc.home.beat.render.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;
import com.demo.scs.poc.commons.pssignal.SignalResponseType;
import com.demo.scs.poc.commons.pssignal.SignalType;
import com.demo.scs.poc.home.beat.render.service.LinkExpander;

@Controller
public class Template3Controller {

    @JsonIgnoreProperties
    public static class Data {

        private String text;
        private String link;

        public String getText() {
            return text;
        }

        public Template3Controller.Data setText(String text) {
            this.text = text;
            return this;
        }

        public String getLink() {
            return link;
        }

        public Template3Controller.Data setLink(String link) {
            this.link = link;
            return this;
        }
    }

    public static class TemplateData {

        public final String text;
        public final String link;
        public final String signalImgUrl;

        public TemplateData(String text, String link, String signalImgUrl) {
            this.text = text;
            this.link = link;
            this.signalImgUrl = signalImgUrl;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(Template3Controller.class);

    @Autowired
    private LinkExpander linkExpander;

    @Autowired
    private PsSignalUrlService psSignalUrlService;

    @Timed
    @ExceptionMetered
    @PostMapping("/template3")
    public String template1(
            Model model,
            @RequestParam String beatId,
            @RequestParam String serviceId,
            @RequestParam String pageId,
            @RequestBody Template3Controller.Data data
    ) {
        LOG.debug("Preparing template3 fragment snipped ...");
        String link = linkExpander.expandLink(data.getLink());
        PsSignalUrlService.Builder builder = psSignalUrlService.builder()
                                                               .beatId(beatId)
                                                               .serviceId(serviceId)
                                                               .pageId(pageId);
        String clickUrl = builder.copy()
                                 .redirect(link)
                                 .type(SignalType.CLICK)
                                 .build();

        String viewUrl = builder.copy()
                                .type(SignalType.DISPLAY)
                                .responseType(SignalResponseType.GIF)
                                .build();

        model.addAttribute("data", new TemplateData(data.getText(), clickUrl, viewUrl));

        return "template3-partial";
    }

}
