package com.demo.scs.poc.vehicle.ui;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractRestTemplateOAuthTest implements RestTemplateHolder {

    @Value("http://localhost:${local.server.port}")
    protected String host;

    @LocalServerPort
    protected int port;

    protected RestOperations template = new RestTemplate();

    @Rule
    public OAuth2ContextSetup context = OAuth2ContextSetup.standard(this);

    @Override
    public void setRestTemplate(final RestOperations restTemplate) {
        this.template = restTemplate;
    }

    @Override
    public RestOperations getRestTemplate() {
        return template;
    }

}

class UserDetails extends ResourceOwnerPasswordResourceDetails {

    public UserDetails(final Object obj) {
        AbstractRestTemplateOAuthTest it = (AbstractRestTemplateOAuthTest) obj;
        setAccessTokenUri(it.host + "/oauth/token");
        setClientId("myclientwith");
        setUsername("user");
        setPassword("password");
    }
}

