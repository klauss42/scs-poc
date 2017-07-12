package com.demo.scs.poc.vehicle.service1;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.scs.poc.vehicle.service1.api.NewCar;
import com.demo.scs.poc.vehicle.service1.domain.Car;
import com.demo.scs.poc.vehicle.service1.service.CarService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTests extends AbstractWebIntegrationTest {

    private static final String HATEAOS_MEDIATYPE = "application/hal+json;charset=UTF-8";

    @Autowired
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestPostProcessor bearerToken;

    @Before
    public void init() {
        carService.deleteAll();
        bearerToken = authHelper.addBearerToken("user");
    }

    @Test
    public void getCars() throws Exception {
        carService.save(new Car().setId(11L));
        carService.save(new Car().setId(22L));
        carService.save(new Car().setId(33L));

        ResultActions resultActions = mvc.perform(get("/car").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(content().contentType(HATEAOS_MEDIATYPE)).andExpect(
            jsonPath("$._embedded.carResourceList", hasSize(3))).andExpect(
            jsonPath("$._embedded.carResourceList[0].carId", isOneOf(11, 22, 33))).andExpect(
            jsonPath("$._embedded.carResourceList[1].carId", isOneOf(11, 22, 33))).andExpect(
            jsonPath("$._embedded.carResourceList[2].carId", isOneOf(11, 22, 33)));
    }

    @Test
    public void getCar() throws Exception {
        Car car = carService.save(new Car().setId(11L));
        ResultActions resultActions = mvc.perform(get("/car/" + car.getId()).with(bearerToken));
        //            .andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(content().contentType(HATEAOS_MEDIATYPE)).andExpect(
            jsonPath("$.carId", is(11)));
    }

    @Test
    public void createCar() throws Exception {
        NewCar car = new NewCar().setVin("123456");
        //CREATE
        MvcResult result = mvc.perform(post("/car").with(bearerToken).content(toJson(car))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            //            .andDo(print())
            .andExpect(status().isCreated()).andExpect(redirectedUrlPattern("**/car/*")).andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/car/" + id).with(bearerToken).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.vin", is(car.getVin())));
    }

    /* PUT */
    @Test
    public void updateCar() throws Exception {
        Car original = carService.save(new Car().setId(1L).setModel("Q7"));

        NewCar updated = new NewCar().setModel("A8");
        MvcResult result = mvc.perform(put("/car/" + original.getId()).with(bearerToken).content(toJson(updated))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            //            .andDo(print())
            .andExpect(status().isOk()).andExpect(redirectedUrlPattern("**/car/*")).andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/car/" + id).with(bearerToken).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.vin", is(updated.getVin())));
    }

    /* DELETE */
    @Test
    public void deleteCar() throws Exception {
        Car original = carService.save(new Car().setId(1L));

        mvc.perform(delete("/car/" + original.getId()).with(bearerToken).accept(MediaType.APPLICATION_JSON)).andExpect(
            status().isNoContent());

        //RETRIEVE
        mvc.perform(get("/car/" + original.getId()).with(bearerToken).accept(MediaType.APPLICATION_JSON)).andExpect(
            status().isNotFound());
    }

    /* DELETE */
    @Test
    public void deleteAllCars() throws Exception {
        mvc.perform(delete("/car").with(bearerToken).accept(MediaType.APPLICATION_JSON)).andExpect(
            status().isNoContent());

        assertThat(carService.findAll().size(), is(0));
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
