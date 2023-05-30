package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.foodcourtmicroservice.configuration.ControllerAdvisor;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoCategoryFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDishFoundException;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    private final String RESPONSE_MESSAGE_KEY_EXPECTED = "message";
    private final String DISH_CREATED_MESSAGE_EXPECTED =  "Dish created";
    private final String USER_PROVIDED_DOES_NOT_HAVE_PERMISSION_MESSAGE_EXPECTED =  "The user provided does not have permission";
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

    private <T> T jsonToObject(String json,Class<T> classToConvert) throws JsonProcessingException {
        return new ObjectMapper().readValue(json,classToConvert );
    }

    private NewDishInfoRequestDto validNewDishInfoRequest(){
        return new NewDishInfoRequestDto(
                        "Pizza Margherita",
                        "Tomato sauce, mozzarella, and basil",
                        "https://www.example.com/pizza-margherita.jpg",
                        10,
                        1L,
                        2L,
                        3L);
    }

    private EditDishRequestDto validEditDishRequestDto(){
        return new EditDishRequestDto(2L,1000,"description edited");
    }



    @Test
    void createNewDishTest_created() throws Exception {
        Long idDish = 199L;
        DishResponseDto expectedResponse = DishResponseDto.builder()
                .id(idDish)
                .active(true)
                .price(1000)
                .description("Description edited")
                .idCategory(1L)
                .idRestaurant(1L)
                .name("Mexican explosion")
                .urlImage("image.com")
                .build();
        when(dishHandler.saveDish(any(NewDishInfoRequestDto.class))).thenReturn(expectedResponse);

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validNewDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(),response.getStatus()),
                () -> assertEquals(mapToJson(expectedResponse),response.getContentAsString()),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class))
        );
    }

    @Test
    void createNewDishTest_failValidationPrice() throws Exception{
        NewDishInfoRequestDto dishInfoRequestDtoBadPrice = validNewDishInfoRequest();
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
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "X MESSAGE");
        doThrow(new UserHasNoPermissionException("X MESSAGE")).when(dishHandler).saveDish(any(NewDishInfoRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validNewDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class)));
    }

    @Test
    void createNewDishTest_noRestaurantFoundException() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "Restaurant not found");
        doThrow(new NoRestaurantFoundException()).when(dishHandler).saveDish(any(NewDishInfoRequestDto.class));

        MockHttpServletResponse response = mockMvc.perform(post("/dishes")
                        .content(mapToJson(validNewDishInfoRequest()))
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
                        .content(mapToJson(validNewDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).saveDish(any(NewDishInfoRequestDto.class)));
    }

    @Test
    void editDishTest_edited() throws Exception {
        Long idDish = 1L;
        DishResponseDto expectedResponse = DishResponseDto.builder()
                .id(idDish)
                .active(true)
                .price(1000)
                .description("Description edited")
                .idCategory(1L)
                .idRestaurant(1L)
                .name("Mexican explosion")
                .urlImage("image.com")
                .build();
        when(dishHandler.editDish(eq(idDish), any(EditDishRequestDto.class))).thenReturn(expectedResponse);

        MockHttpServletResponse response = mockMvc.perform(patch("/dishes/{id}",idDish)
                        .content(mapToJson(validNewDishInfoRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        DishResponseDto responseDto = jsonToObject(response.getContentAsString(), DishResponseDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(expectedResponse.getId(), responseDto.getId()),
                () -> assertEquals(expectedResponse.getName(), responseDto.getName()),
                () -> assertEquals(expectedResponse.getDescription(), responseDto.getDescription()),
                () -> assertEquals(expectedResponse.getUrlImage(), responseDto.getUrlImage()),
                () -> assertEquals(expectedResponse.getPrice(), responseDto.getPrice()),
                () -> assertEquals(expectedResponse.getIdCategory(), responseDto.getIdCategory()),
                () -> assertEquals(expectedResponse.getIdRestaurant(), responseDto.getIdRestaurant()),
                () -> assertEquals(expectedResponse.getActive(), responseDto.getActive()),
                () -> verify(dishHandler).editDish(eq(idDish),any(EditDishRequestDto.class))
        );
    }

    @Test
    void editDishTest_dishNotFound() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "Dish not found");
        doThrow(new NoDishFoundException()).when(dishHandler).editDish(eq(1L),any(EditDishRequestDto.class));
        MockHttpServletResponse response = mockMvc.perform(
                 patch("/dishes/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(validEditDishRequestDto())))
                    .andReturn()
                    .getResponse();
        assertAll(
                 () -> assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus()),
                 () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                 () -> verify(dishHandler).editDish(eq(1L),any(EditDishRequestDto.class)));
    }

    @Test
    void editDishTest_userHasNoPermissionException() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "X MESSAGE");
        doThrow(new UserHasNoPermissionException("X MESSAGE")).when(dishHandler).editDish(eq(1L),any(EditDishRequestDto.class));
        MockHttpServletResponse response = mockMvc.perform(
                        patch("/dishes/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(validEditDishRequestDto())))
                .andReturn()
                .getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(),response.getStatus()),
                () -> assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString())),
                () -> verify(dishHandler).editDish(eq(1L),any(EditDishRequestDto.class)));
    }

    @Test
    void editDishTest_invalidPathVariable() throws Exception {
        Map<String, String> expectedResponseBody = Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY_EXPECTED, "Error parsing a request variable");
        MockHttpServletResponse response = mockMvc.perform(
                        patch("/dishes/{id}", "asda")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(validEditDishRequestDto())))
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException);
                })
                .andExpect(result -> {
                    String errorMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
                    assertTrue(errorMessage.contains("asda"));
                })
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(expectedResponseBody,jsonToMap(response.getContentAsString()));
    }

    @Test
    void editDishTest_failValidationPrice() throws Exception{
        EditDishRequestDto editDishRequestDtoWithBadPrice = validEditDishRequestDto();
        editDishRequestDtoWithBadPrice.setPrice(-1);

        MockHttpServletResponse response = mockMvc.perform(patch("/dishes/{id}",1)
                        .content(mapToJson(editDishRequestDtoWithBadPrice))
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
}