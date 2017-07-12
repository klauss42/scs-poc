package com.demo.scs.poc.vehicle.ui;

import static com.demo.scs.poc.beats.api.v1.BeatProvider.BEAT_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehicleUiApplicationTests extends AbstractRestTemplateOAuthTest {

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
    @OAuth2ContextConfiguration(TestUserDetails.class)
    public void page1() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/page1", String.class);
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

    @Test
    @OAuth2ContextConfiguration(TestUserDetails.class)
    public void beatById() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + BEAT_PATH + "?id=vehicle:1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}



