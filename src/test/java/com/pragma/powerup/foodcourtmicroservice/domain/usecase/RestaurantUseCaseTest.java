package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort mockRestaurantPersistancePort;
    @Mock
    private IUserValidationComunicationPort mockUserValidationComunicationPort;

    private RestaurantUseCase restaurantUseCaseUnderTest;

    @BeforeEach
    void setUp() {
        restaurantUseCaseUnderTest =
                new RestaurantUseCase(
                        mockRestaurantPersistancePort, mockUserValidationComunicationPort);
    }

    @Test
    void isTheRestaurantOwner_userIsOwner() {
        Long idUser = 1L;
        Long idOwner = 1L;
        Restaurant restaurant =
                new Restaurant(
                        1L,
                        "Restaurant 1",
                        "Address 1",
                        idOwner,
                        "1234567890",
                        "logo.png",
                        "123456789");

        Boolean result = restaurantUseCaseUnderTest.isTheRestaurantOwner(idUser, restaurant);

        assertTrue(result);
    }

    @Test
    void isTheRestaurantOwnerTest_userIsNotOwner() {
        Long idUser = 1L;
        Long idOwner = 2L;
        Restaurant restaurant =
                new Restaurant(
                        1L,
                        "Restaurant 1",
                        "Address 1",
                        idOwner,
                        "1234567890",
                        "logo.png",
                        "123456789");

        Boolean result = restaurantUseCaseUnderTest.isTheRestaurantOwner(idUser, restaurant);

        assertFalse(result);
    }

    @Test
    void findByIdWTest_notFoundThenThrowException() {
        Long id = 1L;
        when(mockRestaurantPersistancePort.findById(id)).thenReturn(null);

        assertThrows(
                NoRestaurantFoundException.class,
                () -> {
                    restaurantUseCaseUnderTest.findById(id);
                });

        verify(mockRestaurantPersistancePort).findById(id);
    }

    @Test
    void findByIdTest_success() {
        Long id = 1L;
        Restaurant restaurant =
                new Restaurant(
                        id,
                        "Restaurant Name",
                        "Address",
                        1L,
                        "1234567890",
                        "logo.png",
                        "123456789");
        when(mockRestaurantPersistancePort.findById(id)).thenReturn(restaurant);

        Restaurant result = restaurantUseCaseUnderTest.findById(id);

        verify(mockRestaurantPersistancePort).findById(id);
        assert result != null;
        assert result.getId().equals(id);
        assert result.getName().equals("Restaurant Name");
        assert result.getAddress().equals("Address");
        assert result.getIdOwner().equals(1L);
        assert result.getPhone().equals("1234567890");
        assert result.getUrlLogo().equals("logo.png");
        assert result.getNit().equals("123456789");
    }

    @Test()
    void saveRestaurantTest_invalidNameRestaurantException() {
        final Restaurant restaurant = new Restaurant(null, "111", "address", 1L, "+571231", "urlLogo", "123");
        assertThrows(NotValidNameRestaurantException.class, () -> restaurantUseCaseUnderTest.saveRestaurant(restaurant));
    }

    @Test()
    void saveRestaurantTest_invalidPhoneException() {
        final Restaurant restaurant = new Restaurant(null, "name", "address", 1L, "abc", "urlLogo", "123");
        assertThrows(InvalidPhoneException.class, () -> restaurantUseCaseUnderTest.saveRestaurant(restaurant));
    }

    @Test()
    void saveRestaurantTest_nitNotOnlyNumbersException() {
        final Restaurant restaurant = new Restaurant(null, "name", "address", 1L, "+571231", "urlLogo", "123a");
        assertThrows(NotOnlyNumbersException.class, () -> restaurantUseCaseUnderTest.saveRestaurant(restaurant));
    }

    @Test()
    void saveRestaurantTest_userHasNoPermissionException() {
        final Restaurant restaurant = new Restaurant(null, "name", "address", 1L, "+571231", "urlLogo", "123");
        when(mockUserValidationComunicationPort.userHasRole(1L, Constants.OWNER_ROLE_ID)).thenReturn(false);

        assertThrows(UserHasNoPermissionException.class, () -> restaurantUseCaseUnderTest.saveRestaurant(restaurant));

        verify(mockUserValidationComunicationPort).userHasRole(1L, Constants.OWNER_ROLE_ID);
    }

    @Test()
    void savingRestaurantTest() {
        // Setup
        final Restaurant restaurant = new Restaurant(null, "name", "address", 1L, "+571231", "urlLogo", "123");
        when(mockUserValidationComunicationPort.userHasRole(1L, Constants.OWNER_ROLE_ID)).thenReturn(true);

        // Run the test
        restaurantUseCaseUnderTest.saveRestaurant(restaurant);

        // Verify the results
        verify(mockUserValidationComunicationPort).userHasRole(1L, Constants.OWNER_ROLE_ID);
        verify(mockRestaurantPersistancePort).saveRestaurant(restaurant);
    }
}
