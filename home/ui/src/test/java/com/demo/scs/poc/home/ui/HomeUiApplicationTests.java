package com.demo.scs.poc.home.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;

import com.demo.scs.poc.common.test.AbstractRestTemplateOAuthTest;
import com.demo.scs.poc.common.test.TestUserDetails;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeUiApplicationTests extends AbstractRestTemplateOAuthTest {

    @Test
    public void contextLoads() {
    }

    @Test(expected = RestClientResponseException.class)
    public void homePageProtected() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/", String.class);
    }

    @Test
    @OAuth2ContextConfiguration(TestUserDetails.class)
    public void homePage() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void healthLoads() {
        try {
            ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/health", String
                .class);
            // just check for UNAUTHORIZED, because health check may return 200 or 503 depending on a running Consul is
            // available or not
            assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, e.getStatusCode());
        }
    }

}
