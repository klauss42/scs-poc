package com.demo.scs.poc.common.test;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

public class TestUserDetails extends ResourceOwnerPasswordResourceDetails {

    public TestUserDetails(final Object obj) {
        AbstractRestTemplateOAuthTest it = (AbstractRestTemplateOAuthTest) obj;
        setAccessTokenUri(it.host + "/oauth/token");
        setClientId("myclientwith");
        setUsername("user");
        setPassword("password");
    }
}
