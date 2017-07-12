package com.demo.scs.poc.nav.fallback;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StreamUtils;
import org.springframework.util.SystemPropertyUtils;

@Component
public class NavFallback {

    private static final class CsrfTokenPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private static final String PH_PARAMETER_NAME = "_csrf.parameterName";
        private static final String PH_TOKEN = "_csrf.token";

        private final CsrfToken token;

        private CsrfTokenPlaceholderResolver(CsrfToken token) {
            this.token = Objects.requireNonNull(token);
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            if (PH_PARAMETER_NAME.equals(placeholderName)) {
                return token.getParameterName();
            }
            if (PH_TOKEN.equals(placeholderName)) {
                return token.getToken();
            }
            return null;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(NavFallback.class);

    private ApplicationContext applicationContext;

    private String template;

    private PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
                                                                             SystemPropertyUtils.PLACEHOLDER_SUFFIX,
                                                                             SystemPropertyUtils.VALUE_SEPARATOR, true);

    @Autowired
    public NavFallback(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void postConstruct() {
        template = processFallbackTemplate();
    }

    public String getNavFallbackHtml(HttpServletRequest req) {
        if (template == null) {
            // if instance was not created as spring component
            template = processFallbackTemplate();
        }
        CsrfToken token = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return template;
        }
        return helper.replacePlaceholders(template, new CsrfTokenPlaceholderResolver(token));
    }

    private String processFallbackTemplate() {
        Resource resource = new ClassPathResource("nav-fallback.html");
        if (!resource.isReadable()) {
            LOG.warn("Resource nav-fallback.html is not readable. Returning empty string.");
            return "";
        }
        try (InputStream inputStream = resource.getInputStream()) {
            String template = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            Environment env = applicationContext.getEnvironment();
            String html = env.resolvePlaceholders(template);
            return html;
        } catch (Exception e) {
            LOG.error("Could not read or process fallback resource nav-fallback.html: " + e.getMessage());
            return "";
        }
    }
}
