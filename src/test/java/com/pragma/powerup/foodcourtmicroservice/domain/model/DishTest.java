package com.pragma.powerup.foodcourtmicroservice.domain.model;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DishTest {

    private Dish dishUnderTest;

    @BeforeEach
    void setUp() {
        dishUnderTest = new Dish();
    }

    @Test
    void testSetName() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setName(""));
        assertEquals("Name is not present",exception.getMessage());
    }

    @Test
    void testSetCategory() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setCategory(null));
        assertEquals("Category is not present",exception.getMessage());
    }

    @Test
    void testSetDescription() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setDescription(""));
        assertEquals("Description is not present",exception.getMessage());
    }

    @Test
    void testSetPrice() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setPrice(0));
        assertEquals("Price is not present or not valid",exception.getMessage());
    }

    @Test
    void testSetRestaurant() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setRestaurant(null));
        assertEquals("Restaurant is not present",exception.getMessage());
    }

    @Test
    void testSetUrlImage() {
        FailValidatingRequiredVariableException exception = assertThrows(FailValidatingRequiredVariableException.class, () -> dishUnderTest.setUrlImage(null));
        assertEquals("Url of image is not present",exception.getMessage());
    }
}
