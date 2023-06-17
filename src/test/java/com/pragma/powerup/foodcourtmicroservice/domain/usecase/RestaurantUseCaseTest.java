package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.domain.adapter.ExternalCommunicationDomainAdapter;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantOrderCommonServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort mockRestaurantPersistancePort;
    @Mock
    private ExternalCommunicationDomainAdapter mockUserValidationComunicationPort;

    @Mock
    private ITokenValidationPort tokenValidationPort;

    @Mock
    private IRestaurantOrderCommonServicePort restaurantOrderCommonServicePort;

    private RestaurantUseCase restaurantUseCaseUnderTest;

    private String TOKEN_MESSAGE = "token";

    @BeforeEach
    void setUp() {
        restaurantUseCaseUnderTest =
                new RestaurantUseCase(
                        mockRestaurantPersistancePort, mockUserValidationComunicationPort, tokenValidationPort, restaurantOrderCommonServicePort);
    }

    @Test
    void isTheRestaurantOwner_idRestaurantIsNull() {
        String tokenJwt = "token";
        Long idRestaurant = null;

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, idRestaurant);
                });
    }

    @Test
    void isTheRestaurantOwnerTest_tokenIsNull() {
        String tokenJwt = null;
        Long idRestaurant = 1L;

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, idRestaurant);
                });
    }

    @Test
    void isTheRestaurantOwnerTest_idUserIsNotPresentInToken() {
        String tokenJwt = "noHayIdUser";
        Long idRestaurant = 1L;
        when(tokenValidationPort.findIdUserFromToken(tokenJwt)).thenThrow(NoIdUserFoundInTokenException.class);

        assertThrows(
                NoIdUserFoundInTokenException.class,
                () -> {
                    restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, idRestaurant);
                });
    }
    @Test
    void isTheRestaurantOwner_userIsNotOwner() {
        String tokenJwt = "token";
        Long idRestaurant = 2L;
        Restaurant restaurant =
                new Restaurant(
                        idRestaurant,
                        "Restaurant Name",
                        "Address",
                        3L,
                        "1234567890",
                        "urlLogo",
                        "123456789");
        when(restaurantOrderCommonServicePort.findRestaurantById(idRestaurant)).thenReturn(restaurant);
        when(tokenValidationPort.findIdUserFromToken(tokenJwt)).thenReturn(4L);

        Boolean result = restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, idRestaurant);

        assertFalse(result);
        verify(restaurantOrderCommonServicePort).findRestaurantById(idRestaurant);
    }

    @Test
    void isTheRestaurantOwner_successfully() {
        String tokenJwt = "token";
        Long idRestaurant = 2L;
        Long idOwner = 3L;
        Restaurant restaurant =
                new Restaurant(
                        idRestaurant,
                        "Restaurant Name",
                        "Address",
                        idOwner,
                        "1234567890",
                        "urlLogo",
                        "123456789");
        when(restaurantOrderCommonServicePort.findRestaurantById(idRestaurant)).thenReturn(restaurant);
        when(tokenValidationPort.findIdUserFromToken(tokenJwt)).thenReturn(idOwner);

        Boolean result = restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, idRestaurant);

        assertTrue(result);
        verify(restaurantOrderCommonServicePort).findRestaurantById(idRestaurant);
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
        when(restaurantOrderCommonServicePort.findRestaurantById(id)).thenReturn(restaurant);

        Restaurant result = restaurantUseCaseUnderTest.findById(id);

        verify(restaurantOrderCommonServicePort).findRestaurantById(id);
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

    @Test
    void isTheRestaurantOwner_successfullyVariation() {
        String tokenJwt = "token";
        Long idRestaurant = 2L;
        Long idOwner = 3L;
        Restaurant restaurant =
                new Restaurant(
                        idRestaurant,
                        "Restaurant Name",
                        "Address",
                        idOwner,
                        "1234567890",
                        "urlLogo",
                        "123456789");
        when(tokenValidationPort.findIdUserFromToken(tokenJwt)).thenReturn(idOwner);

        Boolean result = restaurantUseCaseUnderTest.isTheRestaurantOwner(tokenJwt, restaurant);

        assertTrue(result);
        verify(tokenValidationPort).findIdUserFromToken(tokenJwt);
    }

    @Test
    void isTheRestaurantOwner_tokenNotPresent() {
        String token = null;
        Restaurant restaurant = new Restaurant();
        assertThrows(FailValidatingRequiredVariableException.class, () -> restaurantUseCaseUnderTest.isTheRestaurantOwner(token,restaurant));

        verify(tokenValidationPort, times(0)).findIdUserFromToken(token);
    }

    @Test
    void isTheRestaurantOwner_restaurantNotPresent() {
        String token = "token";
        Restaurant restaurant = null;
        assertThrows(FailValidatingRequiredVariableException.class, () -> restaurantUseCaseUnderTest.isTheRestaurantOwner(token,restaurant));

        verify(tokenValidationPort, times(0)).findIdUserFromToken(token);
    }

    @Test
    @DisplayName("Should throw an exception when the sizePage parameter is null or <= 0")
    void findAllPagedTest_WhenSizePageIsNull() {
        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(0, 0,TOKEN_MESSAGE);
                });

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(0, -1,TOKEN_MESSAGE);
                });
        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(0, null,TOKEN_MESSAGE);
                });
    }

    @Test
    @DisplayName("Should throw an exception when the page parameter is null or negative")
    void findAllPagedTest_WhenPageIsNullOrNegative() {
        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(null, 10,TOKEN_MESSAGE);
                });

        assertThrows(
                FailValidatingRequiredVariableException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(-1, 10,TOKEN_MESSAGE);
                });
    }

    @Test
    @DisplayName("Should throw an exception when the list of restaurants is empty")
    void findAllPagedTest_WhenListIsEmpty() {
        when(mockRestaurantPersistancePort.findAllPaged(
                2, 10, "name", "ASC")).thenReturn(new ArrayList<>());
        assertThrows(
                NoDataFoundException.class,
                () -> {
                    restaurantUseCaseUnderTest.findAllPaged(2, 10,TOKEN_MESSAGE);
                });
    }
    
    @Test
    @DisplayName("Should return a list of restaurants")
    void findAllPagedTest_successfully() {
        Integer page = 0;
        Integer sizePage = 10;
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(
                new Restaurant(
                        1L, "Restaurant 1", "Address 1", 1L, "1234567890", "logo1.png", "123456"));
        expectedRestaurants.add(
                new Restaurant(
                        2L, "Restaurant 2", "Address 2", 2L, "0987654321", "logo2.png", "654321"));
        when(mockRestaurantPersistancePort.findAllPaged(
                page, sizePage, "name", "ASC")).thenReturn(expectedRestaurants);

        List<Restaurant> actualRestaurants =
                restaurantUseCaseUnderTest.findAllPaged(page, sizePage,TOKEN_MESSAGE);

        assertEquals(expectedRestaurants, actualRestaurants);
        verify(mockRestaurantPersistancePort, times(1))
                .findAllPaged(page, sizePage, "name", "ASC");
    }

    @Test
    void getDurationOfOrdersByRestaurantTest_successfully() {
        Long idRestaurant = 1L;
        Integer page = 0;
        Integer sizePage = 10;
        String token = "validToken";
        Restaurant restaurant = new Restaurant(22L);
        restaurant.setIdOwner(22L);
        List<OrderDurationInfoDto> expected = List.of(new OrderDurationInfoDto());
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(22L);
        when(restaurantOrderCommonServicePort.findRestaurantById(idRestaurant)).thenReturn(restaurant);
        when(restaurantOrderCommonServicePort.getDurationOfFinalizedOrdersByRestaurant(restaurant,page,sizePage)).thenReturn(expected);

        List<OrderDurationInfoDto> result = restaurantUseCaseUnderTest.getDurationOfOrdersByRestaurant(idRestaurant, page, sizePage, token);

        assertEquals(expected,result);
    }

    @Test
    void getDurationOfOrdersByRestaurantTest_isNotTheOwner() {
        Long idRestaurant = 1L;
        Integer page = 0;
        Integer sizePage = 10;
        String token = "validToken";
        Restaurant restaurant = new Restaurant(22L);
        restaurant.setIdOwner(22L);
        List<OrderDurationInfoDto> expected = List.of(new OrderDurationInfoDto());
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(33L);
        when(restaurantOrderCommonServicePort.findRestaurantById(idRestaurant)).thenReturn(restaurant);

        assertThrows(UserHasNoPermissionException.class, () -> restaurantUseCaseUnderTest.getDurationOfOrdersByRestaurant(idRestaurant,page,sizePage,token));
    }

}
