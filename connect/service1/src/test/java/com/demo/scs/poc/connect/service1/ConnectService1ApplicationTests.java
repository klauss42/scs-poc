package com.demo.scs.poc.connect.service1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectService1ApplicationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    @Test
    public void contextLoads() {
    }

    @Test
    public void resourceProtected() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:{port}/resource/", String.class, port);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String auth = response.getHeaders().getFirst("WWW-Authenticate");
        assertTrue("Wrong header: " + auth , auth.startsWith("Bearer"));
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
