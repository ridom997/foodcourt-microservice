package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.FailConnectionToExternalMicroserviceException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.UserNotFoundFeignException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.foodcourtmicroservice.configuration.ControllerAdvisor;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith({SpringExtension.class})
class RestaurantControllerTest {

    private final String RESPONSE_MESSAGE_KEY_EXPECTED = "message";
    private final String RESTAURANT_CREATED_MESSAGE_EXPECTED =  "Restaurant created";
    private final String RESPONSE_ERROR_MESSAGE_KEY_EXPECTED = "error";
    private final String NO_USER_ROLE_FOUND_MESSAGE_EXPECTED = "No user founded with provided id and role";
    private final String INTERNAL_ERROR_APOLOGIZE_MESSAGE_EXPECTED = "Something wrong happened, try again later!.";
    private final String USER_HAS_NO_PERMISSIONS_MESSAGE_EXPECTED = "User does not have permissions to perform this action";

    private RestaurantController restaurantController;

    @MockBean
    private IRestaurantHandler mockRestaurantHandler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        restaurantController = new RestaurantController(mockRestaurantHandler);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).setControllerAdvice(new ControllerAdvisor()).build();
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    private Map<String,String> jsonToMap(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Map.class);
    }

    private RestaurantRequestDto validRestaurantRequestDto(){
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("Test Restaurant");
        restaurantRequestDto.setAddress("123 Main St");
        restaurantRequestDto.setPhone("555");
        restaurantRequestDto.setUrlLogo("https://example.com/logo.png");
        restaurantRequestDto.setNit("123456789");
        restaurantRequestDto.setIdOwner(1L);
        return restaurantRequestDto;
    }

    @Test
    void testCreateNewRestaurant_created() throws Exception {
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("Test Restaurant");
        restaurantRequestDto.setAddress("123 Main St");
        restaurantRequestDto.setPhone("555");
        restaurantRequestDto.setUrlLogo("https://example.com/logo.png");
        restaurantRequestDto.setNit("123456789");
        restaurantRequestDto.setIdOwner(1L);
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_MESSAGE_KEY_EXPECTED, RESTAURANT_CREATED_MESSAGE_EXPECTED);

        MockHttpServletResponse response = mockMvc.perform(post("/restaurants")
                        .content(mapToJson(restaurantRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class))
        );
    }

    @Test
    void testCreateNewRestaurant_failValidationOfNameVariable() throws Exception {
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("123");
        restaurantRequestDto.setAddress("123 Main St");
        restaurantRequestDto.setPhone("555");
        restaurantRequestDto.setUrlLogo("https://example.com/logo.png");
        restaurantRequestDto.setNit("123456789");
        restaurantRequestDto.setIdOwner(1L);

        MockHttpServletResponse response = mockMvc.perform(post("/restaurants")
                        .content(mapToJson(restaurantRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> {
                    String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
                    assertTrue(errorMessage.contains("Name is not valid, it must have at least one non-numeric character"));
                })
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }

    @Test
    void testCreateNewRestaurant_failValidationOfIdOwnerProvided() throws Exception {
        RestaurantRequestDto restaurantRequestDto = validRestaurantRequestDto();
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, NO_USER_ROLE_FOUND_MESSAGE_EXPECTED);
        doThrow(new UserNotFoundFeignException()).when(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/restaurants")
                        .content(mapToJson(restaurantRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class)));
    }

    @Test
    void testCreateNewRestaurant_failConnectionToExternalMicroserviceException() throws Exception {
        RestaurantRequestDto restaurantRequestDto = validRestaurantRequestDto();
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, INTERNAL_ERROR_APOLOGIZE_MESSAGE_EXPECTED);
        doThrow(new FailConnectionToExternalMicroserviceException()).when(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/restaurants")
                        .content(mapToJson(restaurantRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class)));
    }

    @Test
    void testCreateNewRestaurant_userHasNoPermissionToOwnARestaurantException() throws Exception {
        RestaurantRequestDto restaurantRequestDto = validRestaurantRequestDto();
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, USER_HAS_NO_PERMISSIONS_MESSAGE_EXPECTED);
        doThrow(new UserHasNoPermissionException()).when(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/restaurants")
                        .content(mapToJson(restaurantRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(mockRestaurantHandler).saveRestaurant(any(RestaurantRequestDto.class)));
    }
}
