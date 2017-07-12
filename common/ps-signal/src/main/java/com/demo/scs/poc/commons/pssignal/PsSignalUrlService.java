package com.demo.scs.poc.commons.pssignal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PsSignalUrlService {

    @Value("${scspoc.ps-signaltracker.url}")
    private String baseUrl;

    public Builder builder() {
        return new Builder(baseUrl);
    }

    static class KeyValue {
        final String key;
        final String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static final class Builder {

        private final String baseUrl;

        private String cacheBuster = Long.toString(System.currentTimeMillis());

        // @RequestParam(value = "b", required = false)
        private String beatId;

        // @RequestParam(value = "p", required = false)
        private String pseId;

        // @RequestParam(value = "t", required = false)
        private SignalType type;

        // @RequestParam(value = "c", required = false)
//        private String context;
        private String serviceId;
        private String pageId;

        // @RequestParam(value = "mid", required = false)
        private String scsId;

        // @RequestParam(value = "rt", required = false)
        private SignalResponseType responseType;

        // @RequestParam(value = "r", required = false)
        private String redirect;

        private Builder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        private Builder(String baseUrl, String cacheBuster, String beatId, String pseId, SignalType type, String serviceId,
                String pageId, String scsId, SignalResponseType responseType, String redirect) {
            this.baseUrl = baseUrl;
            this.cacheBuster = cacheBuster;
            this.beatId = beatId;
            this.pseId = pseId;
            this.type = type;
            this.serviceId = serviceId;
            this.pageId = pageId;
            this.scsId = scsId;
            this.responseType = responseType;
            this.redirect = redirect;
        }

        public Builder copy() {
            return new Builder(baseUrl)
                    // .cacheBuster(cacheBuster) // create a new value
                    .beatId(beatId)
                    .psEngineId(pseId)
                    .type(type)
                    .serviceId(serviceId)
                    .pageId(pageId)
                    .scsId(scsId)
                    .responseType(responseType)
                    .redirect(redirect);
        }

        public String build() {
            List<KeyValue> params = new ArrayList<>();
            if (beatId != null) {
                params.add(new KeyValue("b", beatId));
            }
            if (pseId != null) {
                params.add(new KeyValue("p", pseId));
            }
            if (type != null) {
                params.add(new KeyValue("t", type.getParamValue()));
            }
            if (serviceId != null) {
                StringBuilder sb = new StringBuilder(serviceId).append(':');
                if (pageId != null) {
                    sb.append(pageId);
                }
                params.add(new KeyValue("c", sb.toString()));
            }
            if (scsId != null) {
                params.add(new KeyValue("mid", scsId));
            }
            if (responseType != null) {
                params.add(new KeyValue("rt", responseType.getParamValue()));
            }
            if (redirect != null) {
                params.add(new KeyValue("r", redirect));
            }
            if (cacheBuster != null) {
                params.add(new KeyValue("cb", cacheBuster));
            }

            StringBuilder sb = new StringBuilder(baseUrl);
            if (params.size() == 0) {
                return sb.toString();
            }

            String encodedParams = params.stream()
                                         .map(kv -> kv.key + "=" + urlEncode(kv.value))
                                         .collect(Collectors.joining("&"));
            sb.append('?').append(encodedParams);

            return sb.toString();
        }

        private static String urlEncode(String val) {
            try {
                return URLEncoder.encode(val, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Could not url encode " + val, e);
            }
        }

        public Builder beatId(String beatId) {
            this.beatId = beatId;
            return this;
        }

        public Builder psEngineId(String pseId) {
            this.pseId = pseId;
            return this;
        }

        public Builder type(SignalType type) {
            this.type = type;
            return this;
        }

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        public Builder scsId(String scsId) {
            this.scsId = scsId;
            return this;
        }

        public Builder responseType(SignalResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder redirect(String redirect) {
            this.redirect = redirect;
            return this;
        }

        public Builder cacheBuster(String cacheBuster) {
            this.cacheBuster = cacheBuster;
            return this;
        }
    }
}
