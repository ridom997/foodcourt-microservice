package com.pragma.powerup.foodcourtmicroservice.configuration;

public class Constants {



    private Constants() {
        throw new IllegalStateException("Utility class");
    }


    public static final Long OWNER_ROLE_ID = 3L;
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong credentials or role not allowed";
    public static final String RESPONSE_BOOLEAN_RESULT_KEY = "result";

    public static final String RESPONSE_IS_THE_RESTAURANT_OWNER_KEY = "isTheRestaurantOwner";

    public static final String NO_ID_USER_FOUND_IN_TOKEN_JWT_MESSAGE = "Id user not found in jwt token";
    public static final String NO_USER_ROLE_FOUND_MESSAGE = "No user founded with provided id and role";
    public static final String SWAGGER_TITLE_MESSAGE = "Food court API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "Food court microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";

    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE =  "Restaurant created";
    public static final String DISH_CREATED_MESSAGE =  "Dish created";
    public static final String ONLY_NUMBERS_REGEX =  "[0-9]+";
    public static final String PHONE_REGEX = "^\\+?\\d{1,12}$";
    public static final String ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX = "^(?!\\d+$)\\D*[\\d\\D]*$";
    public static final String RESTAURANT_NAME_NOT_VALID_MESSAGE = "Restaurant name is not valid";
    public static final String INVALID_PHONE_MESSAGE = "Phone is not valid";
    public static final String INVALID_NUMBER_MESSAGE = "Number is not valid";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String INTERNAL_ERROR_APOLOGIZE_MESSAGE = "Something wrong happened, try again later!.";
    public static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurant not found";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    public static final String DISH_NOT_FOUND_MESSAGE = "Dish not found";
    public static final String ERROR_PARSING_MESSAGE = "Error parsing a request variable";

    public static final String OWNER_ROLE_NAME = "ROLE_OWNER";
    public static final String CLIENT_ROLE_NAME = "ROLE_CLIENT";
    public static final String CLIENT_VALUE = "CLIENT";
    public static final String EMPLOYEE_ROLE_NAME = "ROLE_EMPLOYEE";

    public static final String USER_PROVIDED_DOES_MATCH_WITH_USER_TOKEN_MESSAGE = "User in request doesn't match with user in token";
    public static final String USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE = "User who made the request is not the restaurant owner";
    public static final String PAGE_NOT_VALID_MESSAGE = "The Page index is not present or has a negative value";
    public static final String SIZE_PAGE_NOT_VALID_MESSAGE = "The Page size is not present or is <= 0";
    public static final String ASC_DIRECTION_VALUE = "ASC";
    public static final String INCOMPLETE_REQUEST_PARAMS = "Incomplete request params";
    public static final String NOT_PRESENT_MESSAGE = " is not present";
    public static final String TOKEN_MESSAGE = "Token";

    public static final String USER_IS_NOT_THE_CLIENT_OF_THE_REQUEST_MESSAGE = "User who made the request is not the same of the order";
    public static final Integer PENDING_ORDER_STATUS_INT_VALUE = 1;
    public static final Integer IN_PROGRESS_ORDER_STATUS_INT_VALUE = 2;
    public static final Integer READY_ORDER_STATUS_INT_VALUE = 3;
    public static final Integer DELIVERED_ORDER_STATUS_INT_VALUE = 4;
    public static final Integer CANCELLED_ORDER_STATUS_INT_VALUE = 5;

    public static final String PENDING_ORDER_STATUS_VALUE = "PENDING";
    public static final String IN_PROGRESS_ORDER_STATUS_VALUE = "IN PROGRESS";
    public static final String READY_ORDER_STATUS_VALUE = "READY";
    public static final String DELIVERED_ORDER_STATUS_VALUE = "DELIVERED";
    public static final String CANCELLED_ORDER_STATUS_VALUE = "CANCELLED";
    public static final String ID_RESTAURANT_STRING_VALUE = "idRestaurant";
    public static final String ORDER_STATUS_STRING_VALUE = "Order status";
    public static final String ORDER_READY_SMS_BODY_BASE_MESSAGE = "Hello, your order is READY! your PIN is: ";
}
