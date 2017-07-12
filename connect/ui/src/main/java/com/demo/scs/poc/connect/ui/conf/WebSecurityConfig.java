package com.demo.scs.poc.connect.ui.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * This conditional configuration can convert the pure ResourceServer app into an app that redirects
 * to the auth server for login. It could be useful to make local development easier, because you
 * don't have to start a gateway app to handle the login.
 * For a production setup I think a pure ResourceServer config is more clean and more safe.
 *
 * Remark: If this config is not conditional, the unit tests will not work. Did not investigate further,
 * how to solve this.
 *
 */
@Configuration
@EnableOAuth2Sso
@Order(-20) // important: if not specified the ResourceServer filter is treated before the OAuth2Sso filter
@ConditionalOnProperty(prefix = "scspoc.security", name = "sso-enabled")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests().anyRequest().authenticated().and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }
}
