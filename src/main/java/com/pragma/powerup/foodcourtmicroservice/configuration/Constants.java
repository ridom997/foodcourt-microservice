package com.pragma.powerup.foodcourtmicroservice.configuration;

public class Constants {



    private Constants() {
        throw new IllegalStateException("Utility class");
    }


    public static final Long OWNER_ROLE_ID = 3L;
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong credentials or role not allowed";

    public static final String USER_HAS_NO_PERMISSIONS_MESSAGE = "User does not have permissions to perform this action";
    public static final String NO_USER_ROLE_FOUND_MESSAGE = "No user founded with provided id and role";
    public static final String SWAGGER_TITLE_MESSAGE = "Food court API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "Food court microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";

    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE =  "Restaurant created";
    public static final String ONLY_NUMBERS_REGEX =  "[0-9]+";
    public static final String PHONE_REGEX = "^\\+?\\d{1,12}$";
    public static final String ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX = "^(?!\\d+$)\\D*[\\d\\D]*$";
    public static final String RESTAURANT_NAME_NOT_VALID_MESSAGE = "Restaurant name is not valid";
    public static final String INVALID_PHONE_MESSAGE = "Phone is not valid";
    public static final String INVALID_NUMBER_MESSAGE = "Number is not valid";
    public static final String REQUIRED_VARIABLE_MISSING_MESSAGE = "Required variable is missing";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String INTERNAL_ERROR_APOLOGIZE_MESSAGE = "Something wrong happened, try again later!.";


}
