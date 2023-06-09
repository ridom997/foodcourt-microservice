package com.pragma.powerup.foodcourtmicroservice.configuration;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.exception.InvalidRequestParamException;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.exception.NonUniqueRequestParamException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleParsingExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ERROR_PARSING_MESSAGE));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, WRONG_CREDENTIALS_MESSAGE));
    }

    @ExceptionHandler({UserHasNoPermissionException.class, UnauthorizedFeignException.class})
    public ResponseEntity<Map<String, String>> handleUserHasNoPermission(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundFeignException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundFeign(UserNotFoundFeignException userNotFoundFeignException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NO_USER_ROLE_FOUND_MESSAGE));
    }

    //
    @ExceptionHandler(NotValidNameRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleNotValidNameRestaurantException(NotValidNameRestaurantException notValidNameRestaurantException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_NAME_NOT_VALID_MESSAGE));
    }

    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPhoneException(InvalidPhoneException invalidPhoneException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, INVALID_PHONE_MESSAGE));
    }

    @ExceptionHandler(NotOnlyNumbersException.class)
    public ResponseEntity<Map<String, String>> handleNotOnlyNumbersException(NotOnlyNumbersException notOnlyNumbersException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, INVALID_NUMBER_MESSAGE));
    }

    @ExceptionHandler(FailValidatingRequiredVariableException.class)
    public ResponseEntity<Map<String, String>> handleRFailValidatingRequiredVariableException(FailValidatingRequiredVariableException failValidatingRequiredVariableException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, failValidatingRequiredVariableException.getMessage()));
    }

    @ExceptionHandler(FailConnectionToExternalMicroserviceException.class)
    public ResponseEntity<Map<String, String>> handleFailConnectionToExternalMicroserviceException(FailConnectionToExternalMicroserviceException failConnectionToExternalMicroserviceException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, INTERNAL_ERROR_APOLOGIZE_MESSAGE));
    }

    @ExceptionHandler(NoRestaurantFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoRestaurantFoundException(NoRestaurantFoundException noRestaurantFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(NoCategoryFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoCategoryFoundException(NoCategoryFoundException noCategoryFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, CATEGORY_NOT_FOUND_MESSAGE));
    }
    @ExceptionHandler(NoDishFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDishFoundException(NoDishFoundException noDishFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DISH_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(NoIdUserFoundInTokenException.class)
    public ResponseEntity<Map<String, String>> handleNoIdUserFoundInTokenException(NoIdUserFoundInTokenException noIdUserFoundInTokenException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NO_ID_USER_FOUND_IN_TOKEN_JWT_MESSAGE));
    }

    @ExceptionHandler(NoRoleFoundInTokenException.class)
    public ResponseEntity<Map<String, String>> handleNoRoleFoundInTokenException(NoRoleFoundInTokenException noRoleFoundInTokenException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, noRoleFoundInTokenException.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleNoRoleFoundInTokenException(MissingServletRequestParameterException missingServletRequestParameterException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, INCOMPLETE_REQUEST_PARAMS));
    }

    @ExceptionHandler(InvalidRequestParamException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestParamException(InvalidRequestParamException invalidRequestParamException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, "There is an invalid request param"));
    }
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(NoDataFoundException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, noDataFoundException.getMessage()));
    }

    @ExceptionHandler(NonUniqueRequestParamException.class)
    public ResponseEntity<Map<String, String>> handleNonUniqueRequestParamException(NonUniqueRequestParamException nonUniqueRequestParamException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, nonUniqueRequestParamException.getMessage()));
    }

    @ExceptionHandler(ClientAlreadyHasAnActiveOrderException.class)
    public ResponseEntity<Map<String, String>> handleClientAlreadyHasAnActiveOrderException(ClientAlreadyHasAnActiveOrderException clientAlreadyHasAnActiveOrderException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, "Client has an active order in the selected restaurant."));
    }

    @ExceptionHandler(NoRestaurantAssociatedWithUserException.class)
    public ResponseEntity<Map<String, String>> handleNoRestaurantAssociatedWithUserException(NoRestaurantAssociatedWithUserException noRestaurantAssociatedWithUserException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, "Employee doesn't have a restaurant associated"));
    }
    @ExceptionHandler(BadRequestFeignException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestFeignException(BadRequestFeignException badRequestFeignException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, "Bad request exception from traceability microservice"));
    }

    @ExceptionHandler(GivenPinIsNotCorrectException.class)
    public ResponseEntity<Map<String, String>> handleGivenPinIsNotCorrectException(GivenPinIsNotCorrectException givenPinIsNotCorrectException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, "The given pin is not correct"));
    }

}
