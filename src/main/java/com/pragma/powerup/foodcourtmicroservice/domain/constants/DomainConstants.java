package com.pragma.powerup.foodcourtmicroservice.domain.constants;

public class DomainConstants {
    private DomainConstants(){}
    public static final String USER_IS_NOT_AN_EMPLOYEE_OF_THE_RESTAURANT = "User who made the request is not an employee of the restaurant.";
    public static final String ORDER_DOES_NOT_BELONG_TO_CLIENT = "The order does not belong to the client";
    public static final String NO_ORDERS_FOUND_MESSAGE = "No orders found";
    public static final String PAGE_NOT_VALID_MESSAGE = "The Page index is not present or has a negative value";
    public static final String SIZE_PAGE_NOT_VALID_MESSAGE = "The Page size is not present or is <= 0";
    public static final String NOT_PRESENT_MESSAGE = " is not present";
    public static final String OWNER_ROLE_NAME = "ROLE_OWNER";
    public static final String USER_PROVIDED_DOES_MATCH_WITH_USER_TOKEN_MESSAGE = "User in request doesn't match with user in token";
    public static final String USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE = "User who made the request is not the restaurant owner";
    public static final String CLIENT_ROLE_NAME = "ROLE_CLIENT";
    public static final Integer PENDING_ORDER_STATUS_INT_VALUE = 1;
    public static final Integer IN_PROGRESS_ORDER_STATUS_INT_VALUE = 2;
    public static final Integer READY_ORDER_STATUS_INT_VALUE = 3;
    public static final Integer DELIVERED_ORDER_STATUS_INT_VALUE = 4;
    public static final Integer CANCELLED_ORDER_STATUS_INT_VALUE = 5;
    public static final String ORDER_READY_SMS_BODY_BASE_MESSAGE = "Hello, your order is READY! your PIN is: ";
    public static final String ID_RESTAURANT_STRING_VALUE = "idRestaurant";
    public static final String ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX = "^(?!\\d+$)\\D*[\\d\\D]*$";
    public static final String PHONE_REGEX = "^\\+?\\d{1,12}$";
    public static final String ONLY_NUMBERS_REGEX =  "[0-9]+";
    public static final Long OWNER_ROLE_ID = 3L;
    public static final String TOKEN_MESSAGE = "Token";
    public static final String ASC_DIRECTION_VALUE = "ASC";
    public static final String PENDING_ORDER_STATUS_VALUE = "PENDING";
    public static final String IN_PROGRESS_ORDER_STATUS_VALUE = "IN PROGRESS";
    public static final String READY_ORDER_STATUS_VALUE = "READY";
    public static final String DELIVERED_ORDER_STATUS_VALUE = "DELIVERED";
    public static final String CANCELLED_ORDER_STATUS_VALUE = "CANCELLED";
    public static final String EMPLOYEE_ROLE_NAME = "ROLE_EMPLOYEE";
    public static final String ORDER_STATUS_STRING_VALUE = "Order status";

}
