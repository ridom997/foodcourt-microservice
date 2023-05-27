package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditDishInfoDtoTest {

    @Test
    void createEditDishInfoDto_whenIdOwnerRestaurantIsNull() {
        EditDishInfoDto editDishInfoDto = new EditDishInfoDto();

        Exception exception =
                assertThrows(
                        FailValidatingRequiredVariableException.class,
                        () -> {
                            editDishInfoDto.setIdOwnerRestaurant(null);
                        });

        String expectedMessage = "Id Owner Restaurant is not present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createEditDishInfoDto_whenDescriptionIsNullOrEmpty() {
        EditDishInfoDto editDishInfoDto = new EditDishInfoDto();
        Exception exception = assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    editDishInfoDto.setDescription(null);
                });
        Exception exception2 = assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    editDishInfoDto.setDescription("");
                });
        String expectedMessage = "Description is not present";
        String actualMessage = exception.getMessage();
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage2.contains(expectedMessage));
    }

    @Test
    void createEditDishInfoDto_whenIdDishIsNull() {
        EditDishInfoDto editDishInfoDto = new EditDishInfoDto();

        Exception exception =
                assertThrows(
                        FailValidatingRequiredVariableException.class,
                        () -> {
                            editDishInfoDto.setIdDish(null);
                        });

        String expectedMessage = "idDish is not present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createEditDishInfoDto_validInput() {
        Long idOwnerRestaurant = 1L;
        Long idDish = 2L;
        Integer price = 100;
        String description = "Delicious dish";

        EditDishInfoDto editDishInfoDto =
                new EditDishInfoDto(idOwnerRestaurant, idDish, price, description);

        assertEquals(idOwnerRestaurant, editDishInfoDto.getIdOwnerRestaurant());
        assertEquals(idDish, editDishInfoDto.getIdDish());
        assertEquals(price, editDishInfoDto.getPrice());
        assertEquals(description, editDishInfoDto.getDescription());
    }

    @Test
    void createEditDishInfoDtoWhenPriceIsNullOrNotValidThenThrowException() {
        EditDishInfoDto editDishInfoDto = new EditDishInfoDto();

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    editDishInfoDto.setPrice(null);
                });

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    editDishInfoDto.setPrice(0);
                });

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    editDishInfoDto.setPrice(-1);
                });
    }
}