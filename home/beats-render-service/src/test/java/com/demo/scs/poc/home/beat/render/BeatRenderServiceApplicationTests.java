package com.demo.scs.poc.home.beat.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;

import com.demo.scs.poc.common.test.AbstractRestTemplateOAuthTest;
import com.demo.scs.poc.common.test.TestUserDetails;
import com.demo.scs.poc.home.beat.render.controller.Template1Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BeatRenderServiceApplicationTests extends AbstractRestTemplateOAuthTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void contextLoads() {
    }

    @Test(expected = RestClientResponseException.class)
    public void resourceProtected() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/", String.class);
    }

    @Test
    @OAuth2ContextConfiguration(TestUserDetails.class)
    public void templateLoads() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Template1Controller.Data data = new Template1Controller.Data().setLink("connect-ui/page1").setText("some text");
        String json = objectMapper.writeValueAsString(data);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> response = template.postForEntity(
            "http://localhost:{port}/template1?beatId=4711&serviceId=junit&pageId=test", httpEntity, String.class,
            port);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //    @Test
    //    public void templateProtected() {
    //        ResponseEntity<String> response = template.getForEntity("http://localhost:{port}/template1/", String
    // .class, port);
    //        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    //        String auth = response.getHeaders().getFirst("WWW-Authenticate");
    //        assertTrue("Wrong header: " + auth , auth.startsWith("Bearer"));
    //    }

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

