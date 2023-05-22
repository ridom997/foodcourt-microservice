package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.foodcourtmicroservice.configuration.ControllerAdvisor;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoCategoryFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoRestaurantFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    private final String RESPONSE_MESSAGE_KEY_EXPECTED = "message";
    private final String DISH_CREATED_MESSAGE_EXPECTED =  "Dish created";
    private final String RESPONSE_ERROR_MESSAGE_KEY_EXPECTED = "error";

    @Mock
    private IDishHandler dishHandler;

    @InjectMocks
    private DishController dishController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dishController = new DishController(dishHandler);
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).setControllerAdvice(new ControllerAdvisor()).build();
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    private Map<String,String> jsonToMap(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Map.class);
    }

    private NewDishInfoRequestDto validDishInfoRequest(){
        return new NewDishInfoRequestDto(
                        "Pizza Margherita",
                        "Tomato sauce, mozzarella, and basil",
                        "https://www.example.com/pizza-margherita.jpg",
                        10,
                        1L,
                        2L,
                        3L);
    }

    @Test
    void createNewDishTest_created() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_MESSAGE_KEY_EXPECTED, DISH_CREATED_MESSAGE_EXPECTED);
        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class))
        );
    }

    @Test
    void createNewDishTest_failValidationPrice() throws Exception{
        NewDishInfoRequestDto dishInfoRequestDtoBadPrice = validDishInfoRequest();
        dishInfoRequestDtoBadPrice.setPrice(-1);

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(dishInfoRequestDtoBadPrice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> {
                    String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
                    assertTrue(errorMessage.contains("Price must be grater than 0"));
                })
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
        verifyNoInteractions(dishHandler);
    }

    @Test
    void createNewDishTest_userHasNoPermissionException() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "The user provided does not have permission");
        doThrow(new UserHasNoPermissionException()).when(dishHandler).saveDish(any(NewDishInfoRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class)));
    }

    @Test
    void createNewDishTest_noRestaurantFoundException() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "Restaurant not found");
        doThrow(new NoRestaurantFoundException()).when(dishHandler).saveDish(any(NewDishInfoRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class)));
    }

    @Test
    void createNewDishTest_noCategoryFoundException() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "Category not found");
        doThrow(new NoCategoryFoundException()).when(dishHandler).saveDish(any(NewDishInfoRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class)));
    }


}