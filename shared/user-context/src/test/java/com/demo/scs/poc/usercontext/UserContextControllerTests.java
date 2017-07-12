package com.demo.scs.poc.usercontext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.scs.poc.usercontext.api.NewData;
import com.demo.scs.poc.usercontext.domain.Data;
import com.demo.scs.poc.usercontext.repository.DataRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserContextControllerTests {
    private static final String HATEAOS_MEDIATYPE = "application/hal+json;charset=UTF-8";

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private DataRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected OAuthHelper authHelper;

    private RequestPostProcessor bearerToken;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        bearerToken = authHelper.addBearerToken("user");
        repository.deleteAll();
    }

    @Test
    public void getUserContext() throws Exception {
        Data data = new Data().setUserId("willi");
        data.getData().put("key1", "value1");
        data.getData().put("key2", "value2");
        repository.save(data.getUserId(), data);

        ResultActions resultActions = mvc.perform(get("/user/willi/" ).with(bearerToken))
//            .andDo(print())
            ;
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(HATEAOS_MEDIATYPE))
            .andExpect(jsonPath("$.data.key1", is("value1")))
            .andExpect(jsonPath("$.data.key2", is("value2")))
        ;
    }

    @Test
    public void createUserContext() throws Exception {
        NewData data = new NewData().setUserId("123456");
        //CREATE
        MvcResult result = mvc.perform(post("/user/" + data.getUserId()).with(bearerToken)
            .content(toJson(data))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
//            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("**/user/*"))
            .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/user/" + id).with(bearerToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(data.getUserId())))
            ;
    }

    /* PUT */
    @Test
    public void updateUserContext() throws Exception {
        Data original = new Data().setUserId("123456");
        original.getData().put("key1", "value1");
        repository.save(original.getUserId(), original);

        NewData updated = new NewData();
        updated.getData().put("key1", "updated");
        MvcResult result = mvc.perform(put("/user/" + original.getUserId()).with(bearerToken)
            .content(toJson(updated))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(redirectedUrlPattern("**/user/*"))
            .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/user/" + id).with(bearerToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.key1", is("updated")))
        ;
    }

    /* DELETE */
    @Test
    public void deleteUserContext() throws Exception {
        Data original = new Data().setUserId("123456");
        original.getData().put("key1", "value1");
        repository.save(original.getUserId(), original);

        mvc.perform(delete("/user/" + original.getUserId()).with(bearerToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        //RETRIEVE
        mvc.perform(get("/user/" + original.getUserId()).with(bearerToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }

    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
}
